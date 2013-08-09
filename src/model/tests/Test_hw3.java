package model.tests;
import static org.junit.Assert.*;
import model.Model;
import model.basis.Desk;
import model.basis.Figure;
import model.exceptions.IllegalTurnException;
import model.history.Turn;

import org.junit.Test;

public class Test_hw3
{
	/** Tests if men can move to empty diagonal positions by 1, they should. */
	@Test
	public void test01()
    {
    	Desk d1 = new Desk("XXXXXXXX" + // 8
    					   "XXXXXXCX" + // 7
    					   "XXXXXXXX" + // 6
    					   "XXXXXXXX" + // 5
    					   "XXXXXXXX" + // 4
    					   "XXXXXXXX" + // 3
    					   "XBXXXXXX" + // 2
    					   "XXXXXXXX"); // 1
    	
    	Figure f1 = d1.getFigureAt('b', 2);
    	assertTrue("Pohyb bieleho kamena dopredu v SV smere - FAILED", f1.move(d1.getPositionAt('a', 3)));
    	System.out.println("Pohyb bieleho kamena dopredu v SV smere - OK");
    	
    	Figure f2 = d1.getFigureAt('a', 3);
    	assertTrue("Pohyb bieleho kamena dopredu v SZ smere - FAILED", f2.move(d1.getPositionAt('b', 4)));
    	System.out.println("Pohyb bieleho kamena dopredu v SZ smere - OK");
    	
    	Figure f3 = d1.getFigureAt('g', 7);
    	assertTrue("Pohyb cierneho kamena dopredu v JZ smere - FAILED", f3.move(d1.getPositionAt('f', 6)));
    	System.out.println("Pohyb cierneho kamena dopredu v JZ smere - OK");
    	
    	Figure f4 = d1.getFigureAt('f', 6);
    	assertTrue("Pohyb cierneho kamena dopredu v JV smere - FAILED", f4.move(d1.getPositionAt('g', 5)));
    	System.out.println("Pohyb cierneho kamena dopredu v JV smere - OK");
    	
    	Desk d2 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXCX" + // 5
						   "XBXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XXXXXXXX" + // 2
						   "XXXXXXXX"); // 1
    	
    	assertEquals("test01 FAILED", d2, d1);
    	System.out.println("test01 PASSED");
    }

 	/** Tests if figures can move to white positions, they shouldn't. */
	@Test
	public void test02()
   	{
	   	Desk d1 = new Desk("XXXXXXXV" + // 8
				   		   "XXXXXXXX" + // 7
				   		   "XXXXXXXX" + // 6
				   		   "XXXXXXXX" + // 5
				   		   "XXXXXXXX" + // 4
				   		   "XXXXXXXX" + // 3
				   		   "XBXXXXXX" + // 2
				   		   "XXXXXXXX"); // 1

	   	Figure f1 = d1.getFigureAt('h', 8);
	   	assertFalse("Cierna dama sa posunula na biele policko - FAILED", f1.move(d1.getPositionAt('h', 1)));
	   	System.out.println("Cierna dama sa neposunula na biele policko - OK");

	   	Figure f2 = d1.getFigureAt('b', 2);
	   	assertFalse("Biely kamen sa posunul na biele policko dopredu - FAILED", f2.move(d1.getPositionAt('b', 3)));
	   	System.out.println("Biely kamen sa neposunul na biele policko dopredu - OK");
	   	
	   	Figure f3 = d1.getFigureAt('b', 2);
	   	assertFalse("Biely kamen sa posunul na biele policko doprava - FAILED", f3.move(d1.getPositionAt('c', 2)));
	   	System.out.println("Biely kamen sa neposunul na biele policko doprava - OK");
	   	
	   	Figure f4 = d1.getFigureAt('b', 2);
	   	assertFalse("Biely kamen sa posunul na biele policko dozadu - FAILED", f4.move(d1.getPositionAt('b', 1)));
	   	System.out.println("Biely kamen sa neposunul na biele policko dozadu - OK");
	   	
	   	Desk d2 = new Desk("XXXXXXXV" + // 8
				   		   "XXXXXXXX" + // 7
				   		   "XXXXXXXX" + // 6
				   		   "XXXXXXXX" + // 5
				   		   "XXXXXXXX" + // 4
				   		   "XXXXXXXX" + // 3
				   		   "XBXXXXXX" + // 2
	   					   "XXXXXXXX"); // 1

	   	assertEquals("test02 FAILED", d2, d1);
	   	System.out.println("test02 PASSED");
   	}
	
	/** Tests if men can move backwards, they shouldn't. */
	@Test
	public void test03()
    {
    	Desk d1 = new Desk("XXXXXXXX" + // 8
				   		   "XXXXXXCX" + // 7
				   		   "XXXXXXXX" + // 6
				   		   "XXXXXXXX" + // 5
				   		   "XXXXXXXX" + // 4
				   		   "XXXXXXXX" + // 3
				   		   "XBXXXXXX" + // 2
				   		   "XXXXXXXX"); // 1
    	
    	Figure f1 = d1.getFigureAt('b', 2);
    	assertFalse("Biely kamen sa posunul dozadu - FAILED", f1.move(d1.getPositionAt('a', 1)));
    	System.out.println("Biely kamen sa neposunul dozadu - OK");
    	
    	Figure f2 = d1.getFigureAt('g', 7);
    	assertFalse("Cierny kamen sa posunul dozadu - FAILED", f2.move(d1.getPositionAt('f', 8)));
    	System.out.println("Cierny kamen sa neposunul dozadu - OK");
    	
    	Desk d2 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXCX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XBXXXXXX" + // 2
						   "XXXXXXXX"); // 1

    	assertEquals("test03 FAILED", d2, d1);
    	System.out.println("test03 PASSED");
    }
	
	/** Tests if kings can move diagonally forward, they should. */
	@Test
	public void test04()
    {
    	Desk d1 = new Desk("XXXXXVXX" + // 8
				   		   "XXXXXXXX" + // 7
				   		   "XXXXXXXX" + // 6
				   		   "XXXXXXXX" + // 5
				   		   "XXXXXXXX" + // 4
				   		   "XXXXXXXX" + // 3
				   		   "XKXXXXXX" + // 2
				   		   "XXXXXXXX"); // 1

    	Figure f1 = d1.getFigureAt('b', 2);
    	assertTrue("Biela dama sa neposunula diagonalne dopredu - FAILED", f1.move(d1.getPositionAt('f', 6)));
    	System.out.println("Biela dama sa posunula diagonalne dopredu - OK");

    	Figure f2 = d1.getFigureAt('f', 8);
    	assertTrue("Cierna dama sa neposunula diagonalne dopredu - FAILED", f2.move(d1.getPositionAt('g', 7)));
    	System.out.println("Cierna dama sa posunula diagonalne dopredu - OK");
    	
    	Desk d2 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXVX" + // 7
						   "XXXXXKXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XXXXXXXX" + // 2
						   "XXXXXXXX"); // 1

    	assertEquals("test04 FAILED", d2, d1);
    	System.out.println("test04 PASSED");
    }
	
	/** Tests if figures can jump over figures of the same color, they shouldn't. */
	@Test
	public void test05()
    {
    	Desk d1 = new Desk("XXXXXXXC" + // 8
						   "XXXXXXVX" + // 7
						   "XXXXXXXK" + // 6
						   "XXXXXXBX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXVXXX" + // 3
						   "XBXXXCXX" + // 2
						   "BXXXXXXX"); // 1

    	Figure f1 = d1.getFigureAt('a', 1);
    	assertFalse("Biely kamen preskocil biely kamen - FAILED", f1.move(d1.getPositionAt('c', 3)));
    	System.out.println("Biely kamen nepreskocil biely kamen - OK");
    	
    	Figure f2 = d1.getFigureAt('h', 8);
    	assertFalse("Cierny kamen preskocil ciernu damu - FAILED", f2.move(d1.getPositionAt('f', 6)));
    	System.out.println("Cierny kamen nepreskocil ciernu damu - OK");
    	
    	Figure f3 = d1.getFigureAt('h', 6);
    	assertFalse("Biela dama preskocila biely kamen - FAILED", f3.move(d1.getPositionAt('f', 4)));
    	System.out.println("Biela dama nepreskocila biely kamen - OK");
    	
    	Figure f4 = d1.getFigureAt('e', 3);
    	assertFalse("Cierna dama preskocila cierny kamen - FAILED", f4.move(d1.getPositionAt('g', 1)));
    	System.out.println("Cierna dama nepreskocila cierny kamen - OK");
    	
    	Desk d2 = new Desk("XXXXXXXC" + // 8
						   "XXXXXXVX" + // 7
						   "XXXXXXXK" + // 6
						   "XXXXXXBX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXVXXX" + // 3
						   "XBXXXCXX" + // 2
						   "BXXXXXXX"); // 1

    	assertEquals("test05 FAILED", d2, d1);
    	System.out.println("test05 PASSED");
    }
	
	/** Tests if figure can jump over 2 figures at once, it shouldn't. */
	@Test
	public void test06()
    {
    	Desk d1 = new Desk("XXXXXXXV" + // 8
						   "XXXXXXBX" + // 7
						   "XXXXXVXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXCXXXXX" + // 3
						   "XCXXXXXX" + // 2
						   "BXXXXXXX"); // 1

    	Figure f1 = d1.getFigureAt('a', 1);
    	assertFalse("Biely kamen preskocil 2 cierne kamene naraz - FAILED", f1.move(d1.getPositionAt('d', 4)));
    	System.out.println("Biely kamen nepreskocil 2 cierne kamene naraz - OK");

    	Figure f2 = d1.getFigureAt('h', 8);
    	assertFalse("Cierna dama preskocila 2 figurky naraz - FAILED", f2.move(d1.getPositionAt('e', 5)));
    	System.out.println("Cierna dama nepreskocila 2 figurky naraz - OK");
    	
    	Desk d2 = new Desk("XXXXXXXV" + // 8
						   "XXXXXXBX" + // 7
						   "XXXXXVXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXCXXXXX" + // 3
						   "XCXXXXXX" + // 2
						   "BXXXXXXX"); // 1

    	assertEquals("test06 FAILED", d2, d1);
    	System.out.println("test06 PASSED");
    }
	
	/** Tests if figure can jump over figure of opposite color, it should. */
	@Test
	public void test07()
    {
    	Desk d1 = new Desk("XXXXXXXV" + // 8
						   "XXXXXXBX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XCXXXXXX" + // 2
						   "BXXXXXXX"); // 1

    	Figure f1 = d1.getFigureAt('a', 1);
    	assertTrue("Biely kamen nepreskocil cierny kamen - FAILED", f1.move(d1.getPositionAt('c', 3)));
    	System.out.println("Biely kamen preskocil cierny kamen - OK");
    	
    	Figure f2 = d1.getFigureAt('h', 8);
    	assertTrue("Cierna dama nepreskocila biely kamen - FAILED", f2.move(d1.getPositionAt('f', 6)));
    	System.out.println("Cierna dama preskocila biely kamen - OK");
    	
    	Desk d2 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXVXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXBXXXXX" + // 3
						   "XXXXXXXX" + // 2
						   "XXXXXXXX"); // 1

    	assertEquals("test07 FAILED", d2, d1);
    	System.out.println("test07 PASSED");
    }
	
	/** Tests if men change to king after reaching opposite side, they should. */
	@Test
	public void test08()
    {
    	Desk d1 = new Desk("XXXXXXXX" + // 8
						   "XXXXBXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXCXXXXX" + // 3
						   "XBXXXXXX" + // 2
						   "XXXXXXXX"); // 1
    	
    	Figure f1 = d1.getFigureAt('e', 7);
    	assertTrue("Biely kamen sa neposunul o policko diagonalne dopredu - FAILED", f1.move(d1.getPositionAt('f', 8)));
    	System.out.println("Biely kamen sa posunul o policko diagonalne dopredu - OK");
    	
    	Figure f2 = d1.getFigureAt('c', 3);
    	assertTrue("Cierny kamen nepreskocil biely kamen - FAILED", f2.move(d1.getPositionAt('a', 1)));
    	System.out.println("Cierny kamen preskocil biely kamen - OK");
    	
    	Desk d2 = new Desk("XXXXXKXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XXXXXXXX" + // 2
						   "VXXXXXXX"); // 1

    	assertEquals("test08 FAILED (kamene sa nezmenili na damy po dosiahnuti superovej zakladne)", d2, d1);
    	System.out.println("test08 PASSED (kamene sa zmenili na damy po dosiahnuti superovej zakladne)");
    }
	
	/** Tests if king jumps over more than one figure, it shouldn't. */
	@Test
	public void test09()
    {
    	Desk d1 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXCXXXX" + // 4
						   "XXCXXXXX" + // 3
						   "XKXXXXXX" + // 2
						   "XXXXXXXX"); // 1
    	
    	Figure f1 = d1.getFigureAt('b', 2);
    	assertFalse("Dama preskocila viac nez 1 figurku opacnej farby - FAILED", f1.move(d1.getPositionAt('e', 5)));
    	System.out.println("Dama nepreskocila viac nez 1 figurku opacnej farby - OK");
    	
    	Desk d2 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXCXXXX" + // 4
						   "XXCXXXXX" + // 3
						   "XKXXXXXX" + // 2
						   "XXXXXXXX"); // 1

    	assertEquals("test09 FAILED", d2, d1);
    	System.out.println("test09 PASSED");
    }
	
	/** Tests if king can move only in 1 direction in 1 move, it should. */
	@Test
	public void test10()
    {
    	Desk d1 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXKXXX" + // 5
						   "XXXCXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XXXXXXXX" + // 2
						   "XXXXXXXX"); // 1
    	
    	Figure f1 = d1.getFigureAt('e', 5);
    	assertTrue("Dama sa nepohla o 1 policko v SV smere - FAILED", f1.move(d1.getPositionAt('c', 3)));
    	System.out.println("Dama sa pohla o 1 policko v SV smere - OK");
    	
    	Figure f3 = d1.getFigureAt('c', 3);
    	assertTrue("Dama sa nepohla o 2 policka v JV smere", f3.move(d1.getPositionAt('e', 1)));
    	System.out.println("Dama sa pohla o 2 policka v JV smere - OK");
    	
    	Figure f4 = d1.getFigureAt('e', 1);
    	assertFalse("Dama sa pohla vo viac ako 1 smere v 1 tahu - FAILED", f4.move(d1.getPositionAt('e', 7)));
    	System.out.println("Dama sa nepohla vo viac ako 1 smere v 1 tahu - OK");

    	Desk d2 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XXXXXXXX" + // 2
						   "XXXXKXXX"); // 1

    	assertEquals("test10 FAILED", d2, d1);
    	System.out.println("test10 PASSED");
    }
	

	
	/** Tests if men can jump to non-empty field, they shouldn't */
	@Test
	public void test11()
	{
    	Desk d1 = new Desk("XCXXXCXX" + // 8
						   "CXBXVXKX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XCXBXVXK" + // 2
						   "XXBXXXBX"); // 1
    	
    	Figure f1 = d1.getFigureAt('c', 1);
    	assertFalse("Biely kamen skocil na cierny kamen - FAILED", f1.move(d1.getPositionAt('b', 2)));
    	System.out.println("Biely kamen neskocil na cierny kamen - OK");
    	
    	Figure f2 = d1.getFigureAt('c', 1);
    	assertFalse("Biely kamen skocil na biely kamen - FAILED", f2.move(d1.getPositionAt('d', 2)));
    	System.out.println("Biely kamen neskocil na biely kamen - OK");
    	
    	Figure f3 = d1.getFigureAt('g', 1);
    	assertFalse("Biely kamen skocil na ciernu damu - FAILED", f3.move(d1.getPositionAt('f', 2)));
    	System.out.println("Biely kamen neskocil na ciernu damu - OK");
    	
    	Figure f4 = d1.getFigureAt('g', 1);
    	assertFalse("Biely kamen skocil na bielu damu - FAILED", f4.move(d1.getPositionAt('h', 2)));
    	System.out.println("Biely kamen neskocil na bielu damu - OK");
    	
    	Figure f5 = d1.getFigureAt('b', 8);
    	assertFalse("Cierny kamen skocil na cierny kamen - FAILED", f5.move(d1.getPositionAt('a', 7)));
    	System.out.println("Cierny kamen neskocil na cierny kamen - OK");
    	
    	Figure f6 = d1.getFigureAt('b', 8);
    	assertFalse("Cierny kamen skocil na biely kamen - FAILED", f6.move(d1.getPositionAt('c', 7)));
    	System.out.println("Cierny kamen neskocil na biely kamen - OK");
    	
    	Figure f7 = d1.getFigureAt('f', 8);
    	assertFalse("Cierny kamen skocil na ciernu damu - FAILED", f7.move(d1.getPositionAt('e', 7)));
    	System.out.println("Cierny kamen neskocil na ciernu damu - OK");
    	
    	Figure f8 = d1.getFigureAt('f', 8);
    	assertFalse("Cierny kamen skocil na bielu damu - FAILED", f8.move(d1.getPositionAt('g', 7)));
    	System.out.println("Cierny kamen neskocil na bielu damu - OK");
    	
    	Desk d2 = new Desk("XCXXXCXX" + // 8
						   "CXBXVXKX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XCXBXVXK" + // 2
						   "XXBXXXBX"); // 1
    	
    	assertEquals("test11 FAILED", d2, d1);
    	System.out.println("test11 PASSED");
	}
	
	/** Tests if figure jumps over opponent's figure when possible, it should. 
	 * @throws IllegalTurnException */
	@Test
	public void test12() throws IllegalTurnException
	{
    	Model m1 = new Model();
    	
		Turn t1 = new Turn("c3-b4");
		m1.makeTurn(t1);
		
		Turn t2 = new Turn("d6-c5");
		m1.makeTurn(t2);
		
		Turn t3 = new Turn("b4-a5");

    	assertFalse("Kamen mohol vyhodit figurku, ale posunul sa inam - FAILED", m1.makeTurn(t3));
		System.out.println("Kamen neskocil inam, ked mohol vyhazdovat - OK");
    }
	
	@Test
	public void test13()
	{
		Desk d1 = new Desk("XXXXXXXX" + // 8
				   "XXXXXXXX" + // 7
				   "XXXXXXXX" + // 6
				   "XXXXXXXX" + // 5
				   "XXXXXXXX" + // 4
				   "XXCXXXXX" + // 3
				   "XBXXXXXX" + // 2
				   "XXXXXXXX"); // 1
		
		assertEquals("Metoda copy triedy Desk by mala vratit kopiu dosky - FAILED", d1,d1.getCopy());
		assertFalse("Metoda copy triedy Desk by mala vratit kopiu dosky - FAILED", d1 == d1.getCopy());
		System.out.println("Metoda copy triedy Desk by mala vratit kopiu dosky - OK");
	}
	
	/** Tests king's jumping priority over man's. */
	@Test
	public void test14() throws IllegalTurnException
	{
    	Desk d1 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XCXXXXXX" + // 2
						   "BXKXXXXX"); // 1
    	
    	Model m1 = new Model();
		Turn t1 = new Turn("a1xc3");

    	assertFalse("Skakala figurka, ked mala skakat dama - FAILED", m1.makeTurn(t1));
		System.out.println("Figurka neskocila, ked mohla skakat dama - OK");
    	
    	Desk d2 = new Desk("XXXXXXXX" + // 8
						   "XXXXXXXX" + // 7
						   "XXXXXXXX" + // 6
						   "XXXXXXXX" + // 5
						   "XXXXXXXX" + // 4
						   "XXXXXXXX" + // 3
						   "XCXXXXXX" + // 2
						   "BXKXXXXX"); // 1

    	assertEquals("test14 FAILED", d2, d1);
		System.out.println("test14 PASSED");
	}
	
	/** Tests if figure jumps over opponent's figure when possible, it should. 
	 * @throws IllegalTurnException */
	@Test
	public void test15() throws IllegalTurnException
	{
    	Model m1 = new Model();
    	
		Turn t1 = new Turn("c3-b4");
		m1.makeTurn(t1);
		
		Turn t2 = new Turn("d6-c5");
		m1.makeTurn(t2);
		
		Turn t3 = new Turn("b4-a5");
		m1.makeTurn(t3);
		
		Turn t4 = new Turn("g3-h4");

    	assertFalse("Figurka mohla vyhadzovat, ale skocila nejaka ina, niekam uplne odveci - FAILED", m1.makeTurn(t4));
		System.out.println("Figurka mohla vyhadzovat, inej bolo zakazane skocit - OK");
    }

}