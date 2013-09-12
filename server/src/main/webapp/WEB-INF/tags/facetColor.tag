<%@ tag import="org.n52.geolabel.commons.LabelFacet"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="facet" required="true" type="org.n52.geolabel.commons.LabelFacet"%>
<%@ attribute name="var" required="true" rtexprvalue="false"%>
<%@ attribute name="colorAvailable" required="true"%>
<%@ attribute name="colorAvailableFragment" fragment="true"%>
<%@ attribute name="colorHigherAvailable" required="true"%>
<%@ attribute name="colorHigherAvailableFragment" fragment="true"%>
<%@ variable name-from-attribute="var" alias="facetColor"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${facet.availability == 'AVAILABLE'}">
		<c:set var="facetColor" value="${colorAvailable}" />
		<c:if test="${not empty colorAvailableFragment}">
			<jsp:invoke fragment="colorAvailableFragment"></jsp:invoke>
		</c:if>
	</c:when>

	<c:when test="${facet.availability == 'AVAILABLE_HIGHER'}">
		<c:set var="facetColor" value="${colorHigherAvailable}" />
		<c:if test="${not empty colorHigherAvailableFragment}">
			<jsp:invoke fragment="colorHigherAvailableFragment"></jsp:invoke>
		</c:if>
	</c:when>

	<c:otherwise>
		<c:set var="facetColor" value="#FFFFFF" />
	</c:otherwise>

</c:choose>
<jsp:doBody></jsp:doBody>

