GEO-label-java jsf
======================

Provides a single JSF component to render GEO labels from metadata/feedback documents. 
Supports Facelets and JSP based view technologies.

Namespace URI: `http://www.geolabel.net`

Tagname: `geolabel`

Supported attributes:
<dl>
  <dt>metadataUrl</dt>
  <dd>Reference to a metadata document</dd>

  <dt>metadataContent</dt>
  <dd>Metadata document</dd>

  <dt>feedbackUrl</dt>
  <dd>Reference to a feedback document</dd>

  <dt>feedbackContent</dt>
  <dd>Feedback document</dd>

  <dt>serviceUrl</dt>
  <dd>Specifies the GEO label service endpoint to use. Will use a default service if not specified.</dd>

  <dt>size</dt>
  <dd>Specifies width and height of the GEO label in px</dd>
  
  <dt>async</dt>
  <dd>Boolean indicating whether the GEO label should be rendered in a subsequent 
  partial page rendering request. This is not supported when using JSP-based JSF. Defaults to true.</dd>
  
  <dt>useCache</dt>
  <dd>Boolean indicating if a server-side cache should be used when requesting GEO labels via metadata/feedback URL. 
  Defaults to true.</dd>
  
   <dt>forceDownload</dt>
  <dd>Boolean, when set to true, metadata/feedback documents referenced by URL will be downloaded 
  server-side and posted to the GEO label service. THis is required if the URLs are not publicly 
  available to the GEO label service endpoint.</dd>
  
</dl>