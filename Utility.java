//Enter file contents here
package register;

import java.math.BigDecimal;

public class Utility 
{
	public Utility()
	{
		
	}
	
	public static int getDecimalPlaces(BigDecimal bDecimal) 
	{
	    String string = bDecimal.stripTrailingZeros().toPlainString();
	    int index = string.indexOf(".");
	    return index < 0 ? 0 : string.length() - index - 1;
	}

}
