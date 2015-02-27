<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    
    <sch:title>Validation rules for Sensor Web labels within SOS 2.0</sch:title>
    <sch:ns uri="http://www.opengis.net/sensorML/2.0" prefix="sml"/>
    <sch:ns uri="http://www.opengis.net/swes/2.0" prefix="swes"/>
    <sch:ns uri="http://www.w3.org/1999/xlink" prefix="xlink"/>
    <sch:ns uri="http://www.isotc211.org/2005/gmd" prefix="gmd"/>
    
    <sch:pattern>
        <sch:title>sensor web label via reference (SOS 2.0)</sch:title>
        <sch:rule context="//swes:extension">
            <sch:let name="labelReference" value="geolabel/labelUrl/text()"/>
            
            <sch:report test="$labelReference != ''">
                Sensor web label found as a reference (SOS 2.0)!</sch:report>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern>
        <sch:title>sensor web label inline (SOS 2.0)</sch:title>
        <sch:rule context="//swes:extension">
            <sch:let name="geolabelElement" value="geolabel/svg:svg"/>
            <sch:let name="svgMetadata" value="//svg:metadata/geolabel"/>
            
            <sch:report test="$geolabelElement or $svgMetadata">
                Sensor web label found inline within the document (SOS 2.0)!</sch:report>
        </sch:rule>
    </sch:pattern>
    
</sch:schema>
