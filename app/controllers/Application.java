package controllers;

import play.*;
import play.mvc.*;


import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;

import views.html.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
	
	
	public Result stringsim(String a, String b) {
        
		
		ObjectNode result = Json.newObject();
		
		Similarity sim = new IDFWeightedSimilarity();
        result.put("similarity", sim.compute(a,b));
     
        return ok(result);

  
    }
	
	public Result documentfreq(String token) {
     
	 
	    ObjectNode result = Json.newObject();
		
		Similarity sim = new IDFWeightedSimilarity();
        result.put("idf", IDF.computeIDF(token));
     
        return ok(result);
	
    }

}
