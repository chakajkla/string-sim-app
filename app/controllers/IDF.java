package controllers;

public class IDF {

	//private static final double s = 1;
	private static final double N = 100000;

	private static final double partialHarmonicSum = 12.0901;

	public static double computeIDF(String token) {

		return Math.log(N / computeDF(token));
	}

	// deterministic + zipf
	private static double computeDF(String token) {
		int rank = (token.hashCode() & 0x7fffffff) % (int) N;
		return 1 / (rank * partialHarmonicSum);
	}

}
