package cn.rjtech.admin.cusorderresult;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.StrPool.COMMA;

public class CusOrderResultService extends CusOrderSumService {
    @Inject
    private ScheduProductPlanMonthService productPlanMonthService;
    @Inject
    private CustomerService customerService;

    // 获取客户订单数据
    private List<Record> getCustomerorderqtyrecords(Kv kv) {
        // 获取客户订单数据
        String str = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31";
        kv.set("roleArray", str);
        List<Record> weekorderDatas = Optional.ofNullable(dbTemplate("cusorderresult.weekOrderDatas", kv).find()).orElse(new ArrayList<>());
        List<Record> subcontractSaleOrderOrManualOrderDatas = Optional.ofNullable(dbTemplate("cusorderresult.SubcontractSaleOrderOrManualOrderDatas", kv).find()).orElse(new ArrayList<>());
        List<Record> customerorderqtyDatas = new ArrayList<>();

        for (String iinventoryid : kv.getStr("iinventoryids").split(",")) {
            // 循环客户
            List<String> icustomerids = new ArrayList<>();
            icustomerids.addAll(weekorderDatas.stream().filter(Objects::nonNull).filter(item -> item.getStr("iinventoryid").equals(iinventoryid)).map(item -> {
                return item.getStr("icustomerid");
            }).collect(Collectors.toList()));
            icustomerids.addAll(subcontractSaleOrderOrManualOrderDatas.stream().filter(Objects::nonNull).filter(item -> item.getStr("iinventoryid").equals(iinventoryid)).map(item -> {
                return item.getStr("icustomerid");
            }).collect(Collectors.toList()));
            icustomerids = icustomerids.stream().distinct().collect(Collectors.toList());

            for (String icustomerid : icustomerids) {

                String startdate = kv.getStr("startdate");
                String enddate = kv.getStr("enddate");
                while (!DateUtils.parseDate(startdate).after(DateUtils.parseDate(enddate))) {
                    Record customerorderqtyData = new Record();
                    customerorderqtyData.put("iinventoryid", iinventoryid);
                    customerorderqtyData.put("icustomerid", icustomerid);
                    String time = startdate;
                    String year = Integer.valueOf(time.split("-")[0]).toString();
                    String month = Integer.valueOf(time.split("-")[1]).toString();
                    String day = Integer.valueOf(time.split("-")[2]).toString();
                    customerorderqtyData.put("time", time);
                    BigDecimal weekorderqty = weekorderDatas.stream().filter(Objects::nonNull).filter(item ->
                            StrUtil.equals(item.getStr("iinventoryid"), iinventoryid)
                                    && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                                    && StrUtil.equals(item.getStr("time"), time)).map(item -> {
                        return item.getBigDecimal("qtysum");
                    }).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal subcontractSaleOrderAndManualQty = subcontractSaleOrderOrManualOrderDatas.stream().filter(Objects::nonNull)
                            .filter(item ->
                                    StrUtil.equals(item.getStr("iinventoryid"), iinventoryid)
                                            && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                                            && StrUtil.equals(item.getStr("iyear"), year)
                                            && StrUtil.equals(item.getStr("imonth"), month)).map(item -> {
                                return item.getBigDecimal("qty" + day + "sum");
                            }).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

                    customerorderqtyData.put("qtysum", weekorderqty.add(subcontractSaleOrderAndManualQty));
                    customerorderqtyDatas.add(customerorderqtyData);
                    startdate = DateUtils.getDate(DateUtils.parseDate(startdate), "yyyy-MM-dd", 1, Calendar.DATE);
                }
            }
        }

        return customerorderqtyDatas;
    }

    private List<Record> getDifferenceqtyDatas(Kv kv, List<Record> customerorderqtyDatas, List<Record> pickupplanqtyDatas) {
        List<Record> differenceqtyDatas = new ArrayList<>();
        for (String iinventoryid : kv.getStr("iinventoryids").split(",")) {
            // 循环客户
            List<String> icustomerids = new ArrayList<>();
            icustomerids.addAll(customerorderqtyDatas.stream().filter(item -> item.getStr("iinventoryid").equals(iinventoryid)).map(item -> {
                return item.getStr("icustomerid");
            }).collect(Collectors.toList()));
            icustomerids.addAll(pickupplanqtyDatas.stream().filter(item -> item.getStr("iinventoryid").equals(iinventoryid)).map(item -> {
                return item.getStr("icustomerid");
            }).collect(Collectors.toList()));

            icustomerids = icustomerids.stream().distinct().collect(Collectors.toList());

            for (String icustomerid : icustomerids) {
                String startdate = kv.getStr("startdate");
                String enddate = kv.getStr("enddate");
                while (!DateUtils.parseDate(startdate).after(DateUtils.parseDate(enddate))) {
                    Record differenceqtyData = new Record();
                    differenceqtyData.put("iinventoryid", iinventoryid);
                    differenceqtyData.put("icustomerid", icustomerid);
                    String time = startdate;
                    differenceqtyData.put("time", time);
                    BigDecimal customerorderqty = customerorderqtyDatas.stream().filter(item ->
                            StrUtil.equals(item.getStr("iinventoryid"), iinventoryid)
                                    && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                                    && StrUtil.equals(item.getStr("time"), time)).map(item -> {
                        return item.getBigDecimal("qtysum");
                    }).reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal pickupplanqty = pickupplanqtyDatas.stream().filter(item ->
                            StrUtil.equals(item.getStr("iinventoryid"), iinventoryid)
                                    && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                                    && StrUtil.equals(item.getStr("time"), time)).map(item -> {
                        return item.getBigDecimal("qtysum");
                    }).reduce(BigDecimal.ZERO, BigDecimal::add);


                    differenceqtyData.put("qtysum", customerorderqty.add(pickupplanqty));
                    differenceqtyDatas.add(differenceqtyData);
                    startdate = DateUtils.getDate(DateUtils.parseDate(startdate), "yyyy-MM-dd", 1, Calendar.DATE);
                }
            }
        }

        return differenceqtyDatas;
    }


    public Page<Record> getAdminDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        // 获取存货信息
        Page<Record> page = dbTemplate("cusorderresult.datas", kv).paginate(pageNumber, pageSize);
        ValidationUtils.notNull(page, JBoltMsg.DATA_NOT_EXIST);
        List<Record> datas = page.getList();
        List<String> iinventoryids = datas.stream().map(item -> {
            return item.getStr("iautoid");
        }).collect(Collectors.toList());
        List<String> cinvcodes = datas.stream().map(item -> {
            return item.getStr("cinvcode");
        }).collect(Collectors.toList());
        kv.set("iinventoryids", StringUtils.join(iinventoryids, COMMA)).set("cinvcodes", StringUtils.join(cinvcodes, COMMA));

        // 获取客户订单数据
        List<Record> customerorderqtyDatas = getCustomerorderqtyrecords(kv);
        // 获取取货计划数据
        List<Record> pickupplanqtyDatas = Optional.ofNullable(dbTemplate("cusorderresult.pickupplanqtyDatas", kv).find()).orElse(new ArrayList<>());
        // 获取出货实绩数据
        List<Record> actualshipmentqtyDatas = Optional.ofNullable(dbTemplate("cusorderresult.actualshipmentqtyDatas", kv).find()).orElse(new ArrayList<>());

        // 订单与取货差异
        List<Record> differenceqtyDatas = getDifferenceqtyDatas(kv, customerorderqtyDatas, pickupplanqtyDatas);

        // 循环存货封装客户数据
        for (Record data : datas) {
            List<Record> customers = new ArrayList<>();
            // 存货下的客户
            List<String> customerids = new ArrayList<>();
            customerids.addAll(customerorderqtyDatas.stream().filter(Objects::nonNull)
                    .filter(item -> StrUtil.equals(item.getStr("iinventoryid"), data.getStr("iautoid")))
                    .map(item -> {
                        return item.getStr("icustomerid");
                    }).collect(Collectors.toList()));
            customerids.addAll(pickupplanqtyDatas.stream().filter(Objects::nonNull)
                    .filter(item -> StrUtil.equals(item.getStr("iinventoryid"), data.getStr("iautoid")))
                    .map(item -> {
                        return item.getStr("icustomerid");
                    }).collect(Collectors.toList()));
            // 待优化 获取出货实际客户信息
            customerids.addAll(actualshipmentqtyDatas.stream().filter(Objects::nonNull)
                    .filter(item -> StrUtil.equals(item.getStr("iinventoryid"), data.getStr("iautoid")))
                    .map(item -> {
                        return item.getStr("icustomerid");
                    }).collect(Collectors.toList()));
            // 去重
            customerids = customerids.stream().distinct().collect(Collectors.toList());

            // 循环客户封装行数据
            for (String icustomerid : customerids) {
                Record customer = customerService.findRecordById(icustomerid);
                // 封装客户订单数据
                List<Record> customerorderqtyRecords = customerorderqtyDatas.stream().filter(Objects::nonNull)
                        .filter(item -> StrUtil.equals(item.getStr("iinventoryid"), data.getStr("iautoid"))
                                && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                        )
                        .collect(Collectors.toList());
                if (!customerorderqtyRecords.isEmpty()) {
                    customer.put("customerorderqtydatas", customerorderqtyRecords);
                    customer.put("customerorderqtysum", customerorderqtyRecords.stream().map(item -> {
                        return item.getBigDecimal("qtysum");
                    }).reduce(BigDecimal.ZERO, BigDecimal::add));
                }

                // 封装取货数据
                List<Record> pickupplanqtyRecords = pickupplanqtyDatas.stream().filter(Objects::nonNull)
                        .filter(item -> StrUtil.equals(item.getStr("iinventoryid"), data.getStr("iautoid"))
                                && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                        )
                        .collect(Collectors.toList());
                if (!pickupplanqtyRecords.isEmpty()) {
                    customer.put("pickupplanqtydatas", pickupplanqtyRecords);
                    customer.put(" pickupplanqtyqtysum", pickupplanqtyRecords.stream().map(item -> {
                        return item.getBigDecimal("qtysum");
                    }).reduce(BigDecimal.ZERO, BigDecimal::add));
                }


                // 待优化 获取客户信息对应出货实际数据求和
                List<Record> actualshipmentRecords = actualshipmentqtyDatas.stream().filter(Objects::nonNull)
                        .filter(item -> StrUtil.equals(item.getStr("iinventoryid"), data.getStr("iautoid"))
                                && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                        )
                        .collect(Collectors.toList());
                if (!actualshipmentRecords.isEmpty()) {
                    customer.put("actualshipmentqtydatas", actualshipmentRecords);
                    customer.put("actualshipmentqtysum", actualshipmentRecords.stream().map(item -> {
                        return item.getBigDecimal("qtysum");
                    }).reduce(BigDecimal.ZERO, BigDecimal::add));
                }

                // 封装订单与取货差异
                List<Record> differenceqtyRecords = differenceqtyDatas.stream().filter(Objects::nonNull)
                        .filter(item -> StrUtil.equals(item.getStr("iinventoryid"), data.getStr("iautoid"))
                                && StrUtil.equals(item.getStr("icustomerid"), icustomerid)
                        )
                        .collect(Collectors.toList());
                if (!differenceqtyRecords.isEmpty()) {
                    customer.put("differenceqtydatas", differenceqtyRecords);
                    customer.put("differenceqtysum", differenceqtyRecords.stream().map(item -> {
                        return item.getBigDecimal("qtysum");
                    }).reduce(BigDecimal.ZERO, BigDecimal::add));
                }

                customers.add(customer);
            }

            // 存货封装客户
            data.put("customers", customers);
        }

        page.setList(datas);
        return page;
    }

    public List<Record> testDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> page = dbTemplate("cusorderresult.datas", kv).paginate(pageNumber, pageSize);
        ValidationUtils.notNull(page, JBoltMsg.DATA_NOT_EXIST);
        List<Record> datas = page.getList();
        for (Record data : datas) {
            List<Record> customers = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Record customer = new Record();
                // 客户订单
                List<Record> customerorderqtyDatas = new ArrayList<>();
                // 取货计划
                List<Record> pickupplanqtyDatas = new ArrayList<>();
                // 出货实际
                List<Record> actualshipmentqtyDatas = new ArrayList<>();
                // 订单与取货差异
                List<Record> differenceqtyDatas = new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    // 日期
                    String time = DateUtils.getDate("yyyy-MM-dd", k, Calendar.DATE);
                    Record customerorderqtyData = new Record();
                    customerorderqtyData.put("time", time);
                    customerorderqtyData.put("qtysum", 1);
                    customerorderqtyDatas.add(customerorderqtyData);
                    Record pickupplanqtyData = new Record();
                    pickupplanqtyData.put("time", time);
                    pickupplanqtyData.put("qtysum", 100);
                    pickupplanqtyDatas.add(pickupplanqtyData);
                    Record actualshipmentqtyData = new Record();
                    actualshipmentqtyData.put("time", time);
                    actualshipmentqtyData.put("qtysum", 2);
                    actualshipmentqtyDatas.add(actualshipmentqtyData);
                    Record differenceqtyData = new Record();
                    differenceqtyData.put("time", time);
                    differenceqtyData.put("qtysum", 3000);
                    differenceqtyDatas.add(differenceqtyData);
                }

                // 客户名称
                customer.put("ccusname", "客户名称" + j);
                customer.put("customerorderqtydatas", customerorderqtyDatas);
                customer.put("pickupplanqtydatas", pickupplanqtyDatas);
                customer.put("actualshipmentqtydatas", actualshipmentqtyDatas);
                customer.put("differenceqtydatas", differenceqtyDatas);

                customers.add(customer);
            }
            data.put("customers", customers);
        }
        return datas;
    }

    /**
     * 组装返回的多层map
     *
     * @param inventoryCode
     * @param customerName
     * @param lists
     */
    public static Map addInventory(String inventoryCode, String customerName, List<List<String>> lists) {
        Map<String, Map<String, List<List<String>>>> inventoryMap = new HashMap<>();
        Map<String, List<List<String>>> customerMap = inventoryMap.computeIfAbsent(inventoryCode, k -> new HashMap<>());
        customerMap.put(customerName, lists);
        return inventoryMap;
    }

    public Map setLists(String inventoryCode, String customerName, List<List<String>> lists) {
        Map<String, Map<String, List<List<String>>>> inventoryMap = new HashMap<>();
        Map<String, List<List<String>>> customerMap = inventoryMap.computeIfAbsent(inventoryCode, k -> new HashMap<>());
        customerMap.put(customerName, lists);
        return inventoryMap;
    }

}
