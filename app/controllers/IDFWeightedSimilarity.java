package controllers;

import java.util.HashMap;
import java.util.HashSet;


import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class IDFWeightedSimilarity implements Similarity {

	@Override
	public double compute(String a, String b) {

		// tokenize strings
		String[] A = a.split("\\s");
		String[] B = b.split("\\s");

		HashMap<Integer, Integer> maxIndexMap = new HashMap<>();
		HashMap<Integer, Double> maxValueMap = new HashMap<>();

		int maxIndex = -1;
		double maxValue = 0;

		// compute jaccard
		for (int i = 0; i < A.length; i++) {

			String s1 = A[i];

			for (int j = 0; j < B.length; j++) {

				String s2 = B[j];

				double jaccardSim = computeJaccard(s1, s2);

				
				if (jaccardSim >= maxValue) {
					
					maxValue = jaccardSim;
					maxIndex = j;
				}

			} // for all B tokens

			maxIndexMap.put(i, maxIndex);
			maxValueMap.put(i, maxValue);
		}

		// calculate similarity
		double sim = 0;
		double normal = 0;
		for (int i = 0; i < A.length; i++) {

			//double idfValue = idf(B[maxIndexMap.get(i)]);
			
		 			
		    Promise<JsonNode> jsonPromise = WS.url("http://localhost:9000/rest/document-frequency").setQueryParameter("token", B[maxIndexMap.get(i)]).get().map(response -> {
                                        return response.asJson(); });
										
		    JsonNode result = jsonPromise.get(10000);
		    double idfValue = result.get("idf").asDouble();

			sim += idfValue * maxValueMap.get(i);
			normal += idfValue;
		}

		return sim / normal;
	}

	private double idf(String token) {
		


		return IDF.computeIDF(token);
	}

	private double computeJaccard(String s1, String s2) {

		HashSet<Character> unionSet = new HashSet<>();

		// add s2 to set
		for (int i = 0; i < s2.length(); i++) {
			unionSet.add(s2.charAt(i));
		}

		double intersectionCt = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (unionSet.contains(s1.charAt(i))) {
				intersectionCt++;
			}
		}

		for (int i = 0; i < s1.length(); i++) {
			unionSet.add(s1.charAt(i));
		}

		return intersectionCt / (double) unionSet.size();
	}

}
