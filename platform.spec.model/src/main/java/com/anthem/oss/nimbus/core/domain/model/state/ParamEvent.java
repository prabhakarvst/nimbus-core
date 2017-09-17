/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data @AllArgsConstructor
public class ParamEvent {

	private Action action;
	
	private Param<?> param;
	
	public boolean shouldAllow() {
		final EntityState<?> p;
		if(param.getRootExecution().getAssociatedParam().isLinked()) {
			p = param.getRootExecution().getAssociatedParam().findIfLinked();
		} else {
			p = param;
		}
		
		return shouldAllow(p);
	}
	
	public boolean shouldAllow(EntityState<?> p) {
		
		Domain currentDomain = AnnotationUtils.findAnnotation(p.getConfig().getReferredClass(), Domain.class);
		
		if(currentDomain == null)
			currentDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		
		if(currentDomain == null)
			return false;
		
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		
		ListenerType includeListener = Arrays.asList(currentDomain.includeListeners())
				.stream()
				.filter((listener) -> !Arrays.asList(pModel.excludeListeners()).contains(listener))
				.filter((listenerType) -> containsListener(listenerType))
				.findFirst()
				.orElse(null);
		
		if(includeListener == null)
			return false;
		
		return true;
	}
	
	public boolean containsListener(ListenerType listenerType) {
		return ListenerType.websocket == listenerType;
	}
}
