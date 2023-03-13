package cn.rjtech.enjoy.directive;

import cn.hutool.core.util.ObjectUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;

import java.io.IOException;

/**
 * 括号指令
 *
 * @author Kephon
 */
public class BracketDirective extends Directive {

    private Expr valueExpr;
    private int paraNum;

    @Override
    public void setExprList(ExprList exprList) {
        this.paraNum = exprList.length();
        if (paraNum == 0) {
            throw new ParseException("Wrong number parameter of #bracket directive, one parameter allowed at most", location);
        } else if (paraNum == 1) {
            this.valueExpr = exprList.getExpr(0);
        }
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        if (paraNum == 0) {
            outputNothing(writer);
        } else if (paraNum == 1) {
            outputBracketValue(scope, writer);
        }
    }

    /**
     * 输出空字符
     */
    private void outputNothing(Writer writer) {
        try {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出处理后的value值
     */
    private void outputBracketValue(Scope scope, Writer writer) {
        Object value = valueExpr.eval(scope);
        if (ObjectUtil.isNull(value) || value.toString().trim().length() == 0) {
            outputNothing(writer);
        } else {
            String val = value.toString().trim();
            if (StrKit.isBlank(val)) {
                outputNothing(writer);
            } else {
                try {
                    writer.write("(" + val + ")");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
