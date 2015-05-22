package ${package};

import com.androidtools.viewholder.AbstractViewHolder;
import com.androidtools.viewholder.ViewHolderInject;
<#list importList as one>
import ${one};
</#list>

public class ${class} extends AbstractViewHolder{

  <#list pairList as prop>
  	@ViewHolderInject(id = R.id.${prop.viewId})
	public ${prop.viewType} ${prop.viewId};
  </#list>
	
	public final int getId() {
		return R.layout.${layout};
	}
}

 
