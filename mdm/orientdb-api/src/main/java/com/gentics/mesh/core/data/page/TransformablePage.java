package com.gentics.mesh.core.data.page;

import java.util.ArrayList;
import java.util.List;

import com.gentics.mesh.context.InternalActionContext;
import com.gentics.mesh.core.data.TransformableElement;
import com.gentics.mesh.core.data.branch.TransformableInPage;
import com.gentics.mesh.core.rest.common.ListResponse;
import com.gentics.mesh.core.rest.common.RestModel;
import com.gentics.mesh.util.ETag;

/**
 * A transformable page is a page which contains {@link TransformableElement}. Thus it is possible to compute the etag for the page and transform the page into
 * a rest list model.
 * 
 * @param <T>
 *            Type of the page element
 */
public interface TransformablePage<T extends TransformableElement<? extends RestModel>> extends Page<T>, TransformableInPage<T> {

	default ListResponse<RestModel> transformToRestSync(InternalActionContext ac, int level) {
		List<RestModel> responses = new ArrayList<>();
		for (T element : getWrappedList()) {
			responses.add(element.transformToRestSync(ac, level));
		}
		ListResponse<RestModel> listResponse = new ListResponse<>();
		setPaging(listResponse);
		listResponse.getData().addAll(responses);
		return listResponse;
	}

	/**
	 * Return the eTag of the page. The etag is calculated using the following information:
	 * <ul>
	 * <li>Number of total elements (all pages)</li>
	 * <li>All etags for all found elements</li>
	 * <li>Number of the current page</li>
	 * </ul>
	 * 
	 * @param ac
	 * @return
	 */
	default String getETag(InternalActionContext ac) {
		StringBuilder builder = new StringBuilder();
		builder.append(getTotalElements());
		builder.append(getNumber());
		builder.append(getPerPage());
		for (T element : this) {
			builder.append("-");
			builder.append(element.getETag(ac));
		}
		return ETag.hash(builder.toString());
	}

}
