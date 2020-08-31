<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="authenticated.activity.list.label.title" path="title" width="40%"/>
	<acme:list-column code="authenticated.activity.list.label.startDate" path="startDate" width="60%"/>
</acme:list>
<acme:form>
	<acme:form-return code="authenticated.activity.list.button.return"/>
</acme:form>