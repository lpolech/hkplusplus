package data;

import java.awt.Color;
//http://phrogz.net/css/distinct-colors.html
public class ColorPalette {
	private static int colorNumber = 0;
	private final static int paletteSize = 100;
	
	public static void resetColorNumber()
	{
		ColorPalette.colorNumber = 0;
	}
	
	public static Color getNextColor()
	{
		switch(colorNumber++ % paletteSize)
		{
			case 0:
				return new Color(68,255,0);
			case 1:
				return new Color(230,0,153);
			case 2:
				return new Color(153,38,84);
			case 3:
				return new Color(229,130,115);
			case 4:
				return new Color(242,191,255);
			case 5:
				return new Color(255,0,0);
			case 6:
				return new Color(229,0,61);
			case 7:
				return new Color(83,32,128);
			case 8:
				return new Color(145,115,230);
			case 9:
				return new Color(255,191,208);
			case 10:
				return new Color(255,136,0);
			case 11:
				return new Color(19,140,0);
			case 12:
				return new Color(29,115,98);
			case 13:
				return new Color(127,153,77);
			case 14:
				return new Color(115,145,153);
			case 15:
				return new Color(238,255,0);
			case 16:
				return new Color(127,102,0);
			case 17:
				return new Color(49,89,22);
			case 18:
				return new Color(140,98,70);
			case 19:
				return new Color(0,61,230);
			case 20:
				return new Color(77,57,57);
			case 21:
				return new Color(255,204,0);
			case 22:
				return new Color(0,140,75);
			case 23:
				return new Color(0,58,217);
			case 24:
				return new Color(62,32,64);
			case 25:
				return new Color(140,19,0);
			case 26:
				return new Color(64,51,0);
			case 27:
				return new Color(153,204,180);
			case 28:
				return new Color(0,27,102);
			case 29:
				return new Color(213,163,217);
			case 30:
				return new Color(102,36,26);
			case 31:
				return new Color(178,152,45);
			case 32:
				return new Color(0,242,162);
			case 33:
				return new Color(38,69,153);
			case 34:
				return new Color(242,0,194);
			case 35:
				return new Color(153,87,77);
			case 36:
				return new Color(255,230,128);
			case 37:
				return new Color(0,64,43);
			case 38:
				return new Color(115,125,153);
			case 39:
				return new Color(102,0,82);
			case 40:
				return new Color(230,180,172);
			case 41:
				return new Color(217,206,163);
			case 42:
				return new Color(0,140,112);
			case 43:
				return new Color(0,8,64);
			case 44:
				return new Color(230,115,191);
			case 45:
				return new Color(217,98,54);
			case 46:
				return new Color(102,95,0);
			case 47:
				return new Color(57,230,195);
			case 48:
				return new Color(67,70,89);
			case 49:
				return new Color(242,0,129);
			case 50:
				return new Color(242,153,121);
			case 51:
				return new Color(226,242,0);
			case 52:
				return new Color(26,102,97);
			case 53:
				return new Color(0,0,255);
			case 54:
				return new Color(76,0,41);
			case 55:
				return new Color(115,94,86);
			case 56:
				return new Color(170,179,45);
			case 57:
				return new Color(115,230,222);
			case 58:
				return new Color(0,0,230);
			case 59:
				return new Color(153,38,99);
			case 60:
				return new Color(255,102,0);
			case 61:
				return new Color(148,153,77);
			case 62:
				return new Color(115,153,150);
			case 63:
				return new Color(0,0,115);
			case 64:
				return new Color(102,51,78);
			case 65:
				return new Color(102,41,0);
			case 66:
				return new Color(51,64,0);
			case 67:
				return new Color(0,167,179);
			case 68:
				return new Color(26,26,51);
			case 69:
				return new Color(153,115,135);
			case 70:
				return new Color(64,26,0);
			case 71:
				return new Color(242,255,191);
			case 72:
				return new Color(0,60,64);
			case 73:
				return new Color(163,163,217);
			case 74:
				return new Color(115,0,46);
			case 75:
				return new Color(153,107,77);
			case 76:
				return new Color(161,242,0);
			case 77:
				return new Color(0,194,242);
			case 78:
				return new Color(51,45,89);
			case 79:
				return new Color(242,0,65);
			case 80:
				return new Color(230,195,172);
			case 81:
				return new Color(202,242,121);
			case 82:
				return new Color(38,130,153);
			case 83:
				return new Color(69,38,153);
			case 84:
				return new Color(230,115,145);
			case 85:
				return new Color(255,136,0);
			case 86:
				return new Color(82,89,67);
			case 87:
				return new Color(0,170,255);
			case 88:
				return new Color(145,115,230);
			case 89:
				return new Color(255,191,208);
			case 90:
				return new Color(140,75,0);
			case 91:
				return new Color(56,140,0);
			case 92:
				return new Color(26,77,102);
			case 93:
				return new Color(108,70,140);
			case 94:
				return new Color(76,0,10);
			case 95:
				return new Color(76,50,19);
			case 96:
				return new Color(89,128,64);
			case 97:
				return new Color(172,210,230);
			case 98:
				return new Color(135,115,153);
			case 99:
				return new Color(153,38,54);
		}
		return new Color(125,125,125);
	}
}
