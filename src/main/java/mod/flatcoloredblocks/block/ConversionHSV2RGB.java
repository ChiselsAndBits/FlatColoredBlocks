package mod.flatcoloredblocks.block;

import java.util.HashMap;

public class ConversionHSV2RGB
{
	private static HashMap<Integer, Integer> conversion = new HashMap<Integer, Integer>();

	private static int fromHSV(
			final double in_h,
			final double in_s,
			final double in_v )
	{
		double hueWheel, p, q, t, ff;
		int hueWheelPart;
		double out_r = 0.0, out_g = 0.0, out_b = 0.0;

		if ( in_s <= 0.0 )
		{ // < is bogus, just shuts up warnings
			out_r = in_v;
			out_g = in_v;
			out_b = in_v;
			return rgb( out_r, out_g, out_b );
		}

		hueWheel = in_h * 360.0;
		if ( hueWheel >= 360.0 )
		{
			hueWheel = 0.0;
		}
		hueWheel /= 60.0;
		hueWheelPart = (int) hueWheel;
		ff = hueWheel - hueWheelPart;
		p = in_v * ( 1.0 - in_s );
		q = in_v * ( 1.0 - in_s * ff );
		t = in_v * ( 1.0 - in_s * ( 1.0 - ff ) );

		switch ( hueWheelPart )
		{
			case 0:
				out_r = in_v;
				out_g = t;
				out_b = p;
				break;
			case 1:
				out_r = q;
				out_g = in_v;
				out_b = p;
				break;
			case 2:
				out_r = p;
				out_g = in_v;
				out_b = t;
				break;

			case 3:
				out_r = p;
				out_g = q;
				out_b = in_v;
				break;
			case 4:
				out_r = t;
				out_g = p;
				out_b = in_v;
				break;
			case 5:
			default:
				out_r = in_v;
				out_g = p;
				out_b = q;
				break;
		}

		return rgb( out_r, out_g, out_b );
	}

	private static int rgb(
			final double out_r,
			final double out_g,
			final double out_b )
	{
		final int r = Math.min( 0xff, Math.max( 0x00, (int) ( out_r * 0xff ) ) );
		final int g = Math.min( 0xff, Math.max( 0x00, (int) ( out_g * 0xff ) ) );
		final int b = Math.min( 0xff, Math.max( 0x00, (int) ( out_b * 0xff ) ) );

		return r << 16 | g << 8 | b;
	}

	public static int toRGB(
			final int hsvFromState )
	{
		Integer rgb = conversion.get( hsvFromState );

		if ( rgb == null )
		{
			final int h = hsvFromState >> 16 & 0xff;
			final int s = hsvFromState >> 8 & 0xff;
			final int v = hsvFromState & 0xff;
			rgb = fromHSV( h / 255.0, s / 255.0, v / 255.0 );
			conversion.put( hsvFromState, rgb );
		}

		return rgb;
	}

}
