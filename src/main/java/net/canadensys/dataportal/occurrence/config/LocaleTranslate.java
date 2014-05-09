package net.canadensys.dataportal.occurrence.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.ocpsoft.common.util.Assert;
import org.ocpsoft.rewrite.config.DefaultOperationBuilder;
import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.param.ConfigurableParameter;
import org.ocpsoft.rewrite.param.Parameter;
import org.ocpsoft.rewrite.param.ParameterStore;
import org.ocpsoft.rewrite.param.Parameterized;
import org.ocpsoft.rewrite.param.RegexParameterizedPatternBuilder;
import org.ocpsoft.rewrite.util.ParseTools.CaptureType;

public class LocaleTranslate extends DefaultOperationBuilder implements Rule,Parameterized{

	private String bundle;
	private String lang;
	//private ParameterStore store;
	private Set<String> toTranslate;
	private Set<RegexParameterizedPatternBuilder> toTranslatePath;

	private LocaleTranslate(final String bundle, final String lang)
	{
		Assert.notNull(bundle, "Bundle must not be null.");
		Assert.notNull(bundle, "Language must not be null.");
		this.bundle = bundle;
		this.lang = lang;
		
		toTranslate = new LinkedHashSet<String>();
		//this.expression = new RegexParameterizedPatternParser("[^/]+", pattern);
	}
	
	public static LocaleTranslate bundle(final String bundle, final String lang){
		return new LocaleTranslate(bundle, lang);
	}
	
	public LocaleTranslate translate(final String resource){
		
		toTranslate.add(resource);
		toTranslatePath.add(new RegexParameterizedPatternBuilder(CaptureType.BRACE, "[^/]+", resource));
		
		return this;
	}

	@Override
	public boolean evaluate(Rewrite event, EvaluationContext context) {
        return true;
	}

	@Override
	public void perform(Rewrite event, EvaluationContext context) {
		//context.
		System.out.println("perform");
		//currResource
		//toTranslatePath.toArray(new Path[])[0].
		
		for(RegexParameterizedPatternBuilder currResource: toTranslatePath)
		{
			currResource.build(event, context);
		}
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<String> getRequiredParameterNames() {
		Set<String> result = new LinkedHashSet<String>();
		result.addAll(toTranslate);
		result.add(lang);
		return result;
	}

	@Override
	public void setParameterStore(ParameterStore store) {
		//this.store = store;
		for(String currResource: toTranslate)
		{
			Parameter<?> parameter = store.get(currResource);
            if (parameter instanceof ConfigurableParameter<?>){
            	((ConfigurableParameter<?>) parameter).transposedBy(MyLocaleTransposition.bundle(lang, bundle));
            }
		}
		
	}


}
