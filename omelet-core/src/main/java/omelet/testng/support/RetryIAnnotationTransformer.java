/*******************************************************************************
 *
 * 	Copyright 2014 Springer Science+Business Media Deutschland GmbH
 * 	
 * 	Licensed under the Apache License, Version 2.0 (the "License");
 * 	you may not use this file except in compliance with the License.
 * 	You may obtain a copy of the License at
 * 	
 * 	    http://www.apache.org/licenses/LICENSE-2.0
 * 	
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 *******************************************************************************/
package omelet.testng.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import omelet.common.Utils;
import omelet.data.MethodContext;
import omelet.data.PrettyMessage;

import org.apache.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.IAnnotationTransformer2;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.ITestAnnotation;

/***
 * For Appending Retry Annotation on All Test Cases and creating Map of classes
 * which are having @AfterMethod and @BeforeMethod
 * 
 * @author kapilA
 * 
 */
public class RetryIAnnotationTransformer implements IAnnotationTransformer,
		IAnnotationTransformer2 {
	private static final Logger LOGGER = Logger
			.getLogger(RetryIAnnotationTransformer.class);
	protected static final Map<String,MethodContext> methodContextHolder = new HashMap<String, MethodContext>();
	
	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass,
			Constructor testConstructor, Method testMethod){
		PrettyMessage prettyMessage = new PrettyMessage();
		Thread t = new Thread(prettyMessage);
		t.start();
		if(testMethod != null)
		{
			MethodContext context = new MethodContext(testMethod);
			context.setRetryAnalyser(annotation);
			context.setDataProvider(annotation, testMethod);
			context.prepareData();
			//update methodContextCollection
			methodContextHolder.put(Utils.getFullMethodName(testMethod), context);
		}
		prettyMessage.swtichOffLogging();
		try {
			t.join();
		} catch (InterruptedException e) {
			LOGGER.error(e);
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void transform(IConfigurationAnnotation annotation, Class testClass,
			Constructor testConstructor, Method testMethod) {
		
	}

	public void transform(IDataProviderAnnotation annotation, Method method) {
		//TODO WHY?? Abstract?
		//No need to add any implementation as we are overriding testng interface
	}

	public void transform(IFactoryAnnotation annotation, Method method) {
		//TODO WHY?? Abstract?
		//No need to add any implementation as we are overriding testng interface
	}

}