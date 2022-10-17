package jbolt;

import cn.jbolt.core.db.sql.Sql;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
public class SqlTest{
	@Test
	public void test() {
		Sql sql;
		String var = "a'";
		final String SAFE_VAR = "'a'''";
		final String LIKE_SAFE_VAR = "'%a''%'";
		final String STARTWITH_SAFE_VAR = "'a''%'";
		final String ENDWITH_SAFE_VAR = "'%a'''";
		
		sql = Sql.mysql().insert("jb_user(username,sex)").values(var, 1);
		System.out.println(sql);
		assertTrue(sql.toString().contains(SAFE_VAR));
		
		sql = Sql.mysql().select().from("jb_user").in("username", var);
		System.out.println(sql);
		assertTrue(sql.toString().contains(SAFE_VAR));
		
		sql = Sql.mysql().select().from("jb_user").instr("username", var, true);
		System.out.println(sql);
		assertTrue(sql.toString().contains(SAFE_VAR));
		
		sql = Sql.mysql().select().from("jb_user").startWith("username", var);
		System.out.println(sql);
		assertTrue(sql.toString().contains(STARTWITH_SAFE_VAR));
		
		sql = Sql.mysql().select().from("jb_user").endWith("username", var);
		System.out.println(sql);
		assertTrue(sql.toString().contains(ENDWITH_SAFE_VAR));
		
		sql = Sql.mysql().select().from("jb_user").like("username", var);
		System.out.println(sql);
		assertTrue(sql.toString().contains(LIKE_SAFE_VAR));
		
		sql = Sql.mysql().select().from("jb_user").findInSet(var,"username",true);
		System.out.println(sql);
		assertTrue(sql.toString().contains(SAFE_VAR));
		
		sql = Sql.mysql().select().from("jb_user").findInSet(var,"fdfda'",false);
		System.out.println(sql);
		assertTrue(sql.toString().contains(SAFE_VAR));

		sql = Sql.mysql().update("jb_user").set("username", var).set("sex", 1).eq("id", 1);
		System.out.println(sql);
		assertTrue(sql.toString().contains(SAFE_VAR));
		
		sql = Sql.mysql().delete().from("jb_user").in("username", var);
		System.out.println(sql);
		assertTrue(sql.toString().contains(SAFE_VAR));

		sql=Sql.sqlserver().instr("name","222,333,444",false);
		System.out.println(sql);
		assertTrue(sql.toString().contains("charindex"));
	}

}
