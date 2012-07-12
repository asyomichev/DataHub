package ia.datahub.beans;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Podracer bean 
 */
@Entity
public class Podracer {
  
  @Id	
  public String model;
  
  public String affiliation;
  public String passenger;
  public String era;
  public String manufacturer;
  public String crew;
  public String length;
  public String craftClass;
  public String engines;
  public String productLine;
  public String role;
  public String armament;
  public String maxSpeed;
  
  // Default constructor as required by JPA
  public Podracer() 
  {
	  // Leave everything at defaults
  }
  
  /**
   * A little utility getter with defaults
   * @param attrs
   * @param name
   * @param dv
   * @return
   */
  static String get(Map<String, String> attrs, String name, String dv)
  {
	  if (attrs.containsKey(name)) 
	  {
		return attrs.get(name);  
	  } else
	  {
		  return dv;
	  }
  }
  
  /**
   * Initialize from name/value pairs scraped from the webpage. 
   * Leave missing attributes at default values
   * 
   * @param attributes - name/value 
   */
  public Podracer(Map<String, String> attributes)
  {
	  affiliation 	= get(attributes, "Affiliation", affiliation);
	  passenger 	= get(attributes, "Passengers", passenger);
	  era 			= get(attributes, "Era(s)", era);
	  manufacturer 	= get(attributes, "Manufacturer", manufacturer);
	  model 		= get(attributes, "Model", model);
	  crew 			= get(attributes, "Crew", crew);
	  length 		= get(attributes, "Length", length);
	  craftClass 	= get(attributes, "Class", craftClass);
	  engines 		= get(attributes, "Engine unit(s)", engines);
	  productLine 	= get(attributes, "Product line", productLine);
	  role 			= get(attributes, "Role(s)", role);
	  armament		= get(attributes, "Armament", armament);
	  maxSpeed 		= get(attributes, "Maximum speed", maxSpeed);
  }
}
