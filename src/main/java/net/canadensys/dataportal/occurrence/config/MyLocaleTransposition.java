/*
 * Copyright 2014 Université de Montréal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.canadensys.dataportal.occurrence.config;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.ocpsoft.rewrite.bind.Binding;
import org.ocpsoft.rewrite.bind.Evaluation;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.param.Parameter;
import org.ocpsoft.rewrite.param.Transposition;

/**
 * A {@link Transposition} responsible for translating values from their matching translation from a ResourceBundle file.
 * TODO ResourceBundle file encoding
 * 
 * Requires inverted property files in the form of translated_name=name_to_bind.
 * 
 * @author Christian Gendreau
 */
public class MyLocaleTransposition implements Transposition<String>{

	// shared thread safe map between a String(representing the language) and a ResourceBundle.
	private static Map<String, ResourceBundle> bundleMap = new ConcurrentHashMap<String, ResourceBundle>();

	private final String languageParam;
	private final String bundleName;
	private boolean lowercaseLookup = true;

	private MyLocaleTransposition(final String languageParam, final String bundleName)
	{
		this.languageParam = languageParam;
		this.bundleName = bundleName;
	}
		
	/**
	 * Create a {@link Binding} to a resource bundle.
	 * @param languageParam name of the {@link Parameter} that contains the language.
	 * @param bundleName name of the {@link ResourceBundle}
	 * @return new instance of LocaleBinding
	 */
	public static MyLocaleTransposition bundle(final String languageParam, final String bundleName)
	{
		return new MyLocaleTransposition(languageParam, bundleName);
	}
	
	/**
	 * Indicates if the lookup to the ResourceBundle should be done in lowercase or as provided.
	 * Default value is false so, as provided. If all you resource bundle is in lowercase this 
	 * option will allow case insensitive lookup.
	 * 
	 */
	public void lowerCaseLookup(final boolean lowercaseLookup)
	{
		this.lowercaseLookup = lowercaseLookup;
	}

	@Override
	public String transpose(Rewrite event, EvaluationContext context, String value)
	{
		String transposedValue = null;
		
		//FIXME this is currently failing due to the absence of the languageParam parameter in the context
		//Retrieve the value of lang from the context
		String targetLang = (String) Evaluation.property(this.languageParam).retrieve(event, context);
		targetLang = targetLang.toLowerCase();
		if (value != null)
		{
			if (!bundleMap.containsKey(targetLang))
			{
				Locale locale = new Locale(targetLang);
				try
				{
					ResourceBundle loadedBundle = ResourceBundle.getBundle(bundleName, locale);
					bundleMap.put(targetLang, loadedBundle);
				} catch (MissingResourceException mrEx)
				{
					System.out.println("Cant' find resource bundle");
					// ignore for now
				}
			}
			
			if (bundleMap.containsKey(targetLang)) {
				try
				{
					// can we received more than one path section? e.g./search/service/
					if(lowercaseLookup)
					{
						transposedValue = bundleMap.get(targetLang).getString(value.toLowerCase());
					}
					else
					{
						transposedValue = bundleMap.get(targetLang).getString(value);
					}
				} catch (MissingResourceException mrEx)
				{
					// if not found, do not translate and keep original value
					transposedValue = value;
				}
			}
			else
			{
				// if language is not defined, do not translate and keep original value
				transposedValue = value;
			}
		}
		
		throw new RuntimeException();
		//return transposedValue;
	}
}
