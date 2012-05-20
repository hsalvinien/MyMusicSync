package com.salvinien.utils;

import java.nio.charset.Charset;
import java.util.Vector;

public class Converter
{
	
	public static Vector<Integer> stringToVector( String aString)
	{ //the string is supposed to be like   "[ xxx, xxx, xxx ...]"
		String S = aString.substring(1, aString.length()-1);
		
		String ts[]= S.split(",");
		
		Vector<Integer> v= new Vector<Integer>( ts.length);
		
		for(int i=0; i< ts.length; i++)
		{
			String S2 = ts[i].trim();
			int a= Integer.parseInt(S2);
			v.add( a);
		}
		
		return v;
	}
	public static int byteArrayToInt( byte[] b)
	{
		int r=0;
		int a0 = (b[0]>=0?b[0]:256+b[0]);
		int a1 = (b[1]>=0?b[1]:256+b[1]);
		int a2 = (b[2]>=0?b[2]:256+b[2]);
		int a3 = (b[3]>=0?b[3]:256+b[3]);
		
		r = a0 +256 * a1 + 65536 * a2 + 16777216 * a3;
		
		return r;
	}

	public static int byteArrayToIntMBS( byte[] b)
	{
		int r=0;
		
		if(b.length==3)
		{
			byte[] c= new byte[4];
			c[0]=0;
			c[1]=b[0];
			c[2]=b[1];
			c[3]=b[2];
			b=c;
		}
		
		int a0 = (b[0]>=0?b[0]:256+b[0]);
		int a1 = (b[1]>=0?b[1]:256+b[1]);
		int a2 = (b[2]>=0?b[2]:256+b[2]);
		int a3 = (b[3]>=0?b[3]:256+b[3]);
		
		r = a3 +256 * a2 + 65536 * a1 + 16777216 * a0;
		
		return r;
	}
	
	public static String int2StringKB( long a)
	{
		String s=null;
		
		a/=10000;
		float f = a/100.0f;
		s= String.valueOf(f)+" Mb";
		
		return s;
	}
	
	public static String byteToString( byte[] b)
	{
		String s=null;
		Charset charset =null;
		int offset=0;
		
		if( (b.length <3)||( b[0]>0))  //we suppose that we don't try to create a string wih first byte 0 in ASCII
		{
			//charset = Charset.forName("US-ASCII");
			charset = Charset.forName("UTF-8");
			offset =0; //since it is actually pure ASCII...well a sort of...there is no BOM flag
		}
		else
		{
			
			//charset = Charset.forName("US-ASCII");
			charset = Charset.forName("UTF-8");
			offset =3; 
			
			switch( b[0])
			{
				case 0x0:   /* so this must be a UTF-32BE*/
							if( b.length >=4)
							{
								if( (b[1]==(byte)0x0) && (b[2]==(byte)0xFE) && (b[3]==(byte)0xFF))
								{
									charset = Charset.forName("UTF-32BE");
									offset =4;
								}
							}
								
					break;
					
				case (byte) 0xEF:  /* so this must be a UTF-8*/
						if( (b[1]==(byte)0xBB) && (b[2]==(byte)0xBF) )
						{
							charset = Charset.forName("UTF-8");
							offset =3; 
						}
				
					break;

				case (byte) 0xFE:  /* so this must be a UTF-16BE*/
						if(b[1]==(byte)0xFF)
						{
							charset = Charset.forName("UTF-16BE");
							offset =2;
						}
					break;
				
				case (byte) 0xFF: /*it is either UTF-16LE or UTF-32LE*/
						if(b[1]==(byte)0xFE)
						{
							charset = Charset.forName("UTF-16LE");
							offset =2;
							if( b.length >=4)
							{
								if( (b[3]==(byte)0x0) && (b[2]==(byte)0x0))
								{
									charset = Charset.forName("UTF-32BE");
									offset =4;
								}
							}
						}
			
					break;

				case 0x2B: //never happen since it is caught by the first if
							// so UTF-7 is not supported
						if( b.length >=4)
						{
							if( (b[1]==(byte)0x2F) && (b[2]==(byte)0x76) &&( (b[3]==(byte)0x38)||(b[3]==(byte)0x39)||(b[3]==(byte)0x2B)||(b[3]==(byte)0x2F) ))
							{
								charset = Charset.forName("UTF-7");
								offset =4;
							}
						}
					break;
					
				case (byte) 0xF7:   /* so this must be a UTF-1*/
						if( (b[1]==(byte)0x64) && (b[2]==(byte)0x4C) )
						{
							charset = Charset.forName("UTF-1");
							offset =3;
						}
			
					break;
					
				case (byte) 0xDD:   /* so this must be a UTF-EBCDIC*/
						if( b.length >=4)
						{
							if( (b[1]==(byte)0x73) && (b[2]==(byte)0x66) && (b[3]==(byte)0x73))
							{
								charset = Charset.forName("UTF-EBCDIC");
								offset =4;
							}
						}
						
					break;
					
				case 0x0E:    //never happen since it is caught by the first if
						/* so this must be a SCSU*/
						if( (b[1]==(byte)0xFE) && (b[2]==(byte)0xFF) )
						{
							charset = Charset.forName("SCSU");
							offset =3;
						}
		
				
					break;
					
				case (byte) 0xFB:   /* so this must be a BOCU-1*/
						if( (b[1]==(byte)0x64) && (b[2]==(byte)0x4C) )
						{
							charset = Charset.forName("BOCU-1");
							offset =3;
						}
			
				break;
					
				case (byte) 0x84:   /* so this must be a GB-18030*/
					if( b.length >=4)
					{
						if( (b[1]==(byte)0x31) && (b[2]==(byte)0x95) && (b[3]==(byte)0x33))
						{
							charset = Charset.forName("GB-18030");
							offset =4;
						}
					}
						
					break;
					
			}
		}
		
		byte[] b1 = new byte[ b.length-offset];
		System.arraycopy(b, offset, b1, 0, b.length-offset); //again
		b=b1;
		s= new String( b, charset);
		
		return s;
	}
}
