package io.github.SebastianDanielFrenz.MEPlugin;

public class Utils {

	public static int roundUp(float n) {
		if ((int) n == n) {
			return (int) n;
		}
		return (int) n + 1;
	}

}
