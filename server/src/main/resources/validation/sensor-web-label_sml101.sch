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
    
    <sch:title>Validation rules for sensor web labels within SensorML 1.0.1</sch:title>
    <sch:ns uri="http://www.opengis.net/sensorML/1.0.1" prefix="sml"/>
    <sch:ns uri="http://www.w3.org/1999/xlink" prefix="xlink"/>
    
    <sch:pattern>
        <sch:title>sensor web label as reference (SensorML 1.0.1)</sch:title>
        <sch:rule context="//sml:documentation">
            <sch:let name="labelInFormat" value="contains(sml:Document/sml:format, 'geolabel')"/>
            <sch:let name="role" value="sml:Document/sml:onlineResource/@xlink:role"/>
            <sch:let name="href" value="sml:Document/sml:onlineResource/@xlink:href"/>
            
            <sch:report test="($labelInFormat or $role != '') and $href != ''">
                Sensor web label as reference found (SensorML 1.0.1)!</sch:report>
        </sch:rule>
    </sch:pattern>
    
</sch:schema>
