package ro.fortech.edu.rest;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * A class extending avax.ws.rs.core.Application and annotated with @ApplicationPath
 *  is the Java EE 6 "no XML" approach to activating JAX-RS.
 * <p>
 * Resources are served relative to the servlet path specified in the @ApplicationPath annotation.
 * </p>
 */

@ApplicationPath("/rest/*")
public class JaxRsActivator extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		return null;
	}

}
