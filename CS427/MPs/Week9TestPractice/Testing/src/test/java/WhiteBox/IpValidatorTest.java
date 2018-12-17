package WhiteBox;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IpValidatorTest {

    @Test
    public void testInvalidIPv4AddressTooShort(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("0"));
    }

    @Test
    public void testInvalidIPv4AddressBadFirstChar(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress(".0.0.1.127"));
    }

    @Test
    public void testInvalidIPv4AddressBadLastChar(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("0.0.1.127."));
    }

    @Test
    public void testInvalidIPv4AddressTokenNotNumber(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("0.A.1.127"));
    }

    @Test
    public void testInvalidIPv4AddressNonZeroTokenHas0First(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("1.05.1.127"));
    }

    @Test
    public void testInvalidIPv4AddressTokenOutOfRange(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("1.400.1.127"));
    }

    @Test
    public void testInvalidIPv4AddressTokenZeroButBadFormat(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("1.+0.1.127"));
    }

    @Test
    public void testInvalidIPv4AddressNegativeToken(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("1.0.-1.127"));
    }

    @Test
    public void testValidIPv4Address(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("IPv4",tool.validIPAddress("1.0.0.127"));
    }

    @Test
    public void testValidIPv6Address(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("IPv6",tool.validIPAddress("FE80:CD00:0000:0CDE:1257:0000:211E:729C"));
    }

    @Test
    public void testNegativeZero(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:-000:0CDE:1257:0000:211E:729C"));
    }

    @Test
    public void testNegativeToken(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:-100:0CDE:1257:0000:211E:729C"));
    }

    @Test
    public void testOnlyOneToken(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress(":::::::729C"));
    }

    @Test
    public void testValidIPv6AddressBadFirstChar(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress(":FE80:CD00:0000:0CDE:1257:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressBadLastChar(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257:0000:211E:729C:"));
    }

    @Test
    public void testValidIPv6NotEnoughTokens(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257:0000:211E"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:12570:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength3(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257X:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength4(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257x:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength5(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257a:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength6(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257A:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength7(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257aA:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength8(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257xX:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength9(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257xA:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength10(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1257aX:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenLength2(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE::0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:125X:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars2(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:125x:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars3(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:xXxX:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars4(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:xxxx:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars5(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("IPv6",tool.validIPAddress("FE80:CD00:0000:0CDE:aaaa:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars6(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("IPv6",tool.validIPAddress("FE80:CD00:0000:0CDE:AAAA:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars7(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("IPv6",tool.validIPAddress("FE80:CD00:0000:0CDE:aAaA:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars8(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:XXXX:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars9(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("IPv6",tool.validIPAddress("FE80:CD00:0000:0CDE:aA12:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars10(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:aAxX:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars12(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:aA1X:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars13(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:aA1x:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars14(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:aX1x:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars15(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:AX1x:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressInvalidTokenChars16(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE: :0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressBadFirstChar2(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress(".FE80:CD00:0000:0CDE:1250:0000:211E:729C"));
    }

    @Test
    public void testValidIPv6AddressBadLastChar2(){
        IpValidator tool = new IpValidatorImpl();
        assertEquals("Neither",tool.validIPAddress("FE80:CD00:0000:0CDE:1250:0000:211E:729C."));
    }


    //@TODO: Create more tests
}
