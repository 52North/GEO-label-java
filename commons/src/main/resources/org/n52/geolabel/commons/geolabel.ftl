<#macro facetColor facet colorAvailable colorHigherAvailable colorAvailableFragment="" colorHigherAvailableFragment=""  >

	<#switch facet.availability.name()>
		<#case "AVAILABLE">
			<#if colorAvailableFragment??>
				${colorAvailableFragment}
			</#if>
			<#nested colorAvailable>
		<#break>
		
		<#case "AVAILABLE_HIGHER">
			<#if colorHigherAvailableFragment??>
				${colorHigherAvailableFragment}
			</#if>
			<#nested colorHigherAvailable>
		<#break>
		
		<#default>
			<#nested "#FFFFFF">
	
	</#switch>

</#macro>

<#assign strokeWidth = 1>
<#assign outerStrokeWidth = 2>

<svg version="1.1"
	xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
	width="${size}"	height="${size}" id="${id!""}">
		
	

	<#escape x as x?xml>

	<g class="size_group" id="size_group" transform="scale(${(size/250)?c})">

		<!-- Facets Group Opening -->
		<g class="facets_group" id="facets_group">

			<!-- Producer Profile -->
			<g class="producer_profile" id="producer_profile">
				<title>${label.producerProfileFacet.title!"NA"}</title><#lt>
				<a xlink:href="${label.producerProfileFacet.href!"NA"}" target="_blank">

					<#assign higherFragment>
						<linearGradient id="SVGID_1_" gradientUnits="userSpaceOnUse" x1="171.7363" y1="11.0854" x2="141.5719"	y2="93.9615">
							<stop offset="0.3" style="stop-color:#FFFFFF" />
							<stop offset="0.3417" style="stop-color:#FCD8E9" />
							<stop offset="0.3956" style="stop-color:#F8ADD0" />
							<stop offset="0.4537" style="stop-color:#F586BA" />
							<stop offset="0.5153" style="stop-color:#F366A8" />
							<stop offset="0.5815" style="stop-color:#F14C99" />
							<stop offset="0.6539" style="stop-color:#EF378D" />
							<stop offset="0.7357" style="stop-color:#EE2985" />
							<stop offset="0.835" style="stop-color:#ED2180" />
							<stop offset="1" style="stop-color:#ED1E7F" />
						</linearGradient>
					</#assign>

					<@facetColor colorAvailable="#ED1E7F" colorHigherAvailable="url(#SVGID_1_)"	colorHigherAvailableFragment=higherFragment facet=label.producerProfileFacet ; color>
						
							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M152.178,97.822l59.796-59.795
							C187.958,14.008,156.478,2,125,2l0,84.563C134.837,86.563,144.674,90.316,152.178,97.822z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10"
								d="M154.14,31.925
								c2.306-0.358,5.972,1.384,5.972,1.384c-7.198,7.265-17.221,16.66-21.993,21.108c-1.67-0.165-3.921-0.971-3.979-2.51
								c-0.066-1.755,2.612-3.657,4.608-5.616c4.082-4.008,6.06-6.125,10.018-10.091C150.775,34.184,152.558,32.167,154.14,31.925z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10"
								d="M167.306,26.13
								c1.821,1.689,2.298,2.29,4.055,3.965c-7.401,7.411-22.163,21.813-24.745,24.211c-2.907,2.581-5.549,4.796-5.549,4.796
								l-7.479,3.439l4.013-7.443c0,0,4.047-3.986,6.425-6.279C151.787,41.333,159.915,33.34,167.306,26.13z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10"
								d="M182.787,47.247l-1.194,11.154l-25.367-0.137
								c-4.976,0.13-9.948,0.983-9.535-3.671c0.214-2.416,0.819-3.699,5.114-3.937h8.306c0,0-2.658-1.896-5.663-3.763
								c1.758-1.844,8.417-8.233,11.033-10.81C167.033,36.949,182.787,47.247,182.787,47.247z"></path>
						
					</@facetColor>
				</a>
			</g>
			
			<!-- Lineage -->
			<g class="lineage" id="lineage">
				<title>${label.lineageFacet.title!"NA"}</title><#lt>
				<a xlink:href="${label.lineageFacet.href!"NA"}" target="_blank">
				
				<#assign higherFragment>
					<linearGradient id="SVGID_2_" gradientUnits="userSpaceOnUse" x1="238.9146" y1="171.5801" x2="156.039"
								y2="141.4158">
								<stop offset="0.3" style="stop-color:#FFFFFF" />
								<stop offset="0.3417" style="stop-color:#FCD8E9" />
								<stop offset="0.3956" style="stop-color:#F8ADD0" />
								<stop offset="0.4537" style="stop-color:#F586BA" />
								<stop offset="0.5153" style="stop-color:#F366A8" />
								<stop offset="0.5815" style="stop-color:#F14C99" />
								<stop offset="0.6539" style="stop-color:#EF378D" />
								<stop offset="0.7357" style="stop-color:#EE2985" />
								<stop offset="0.835" style="stop-color:#ED2180" />
								<stop offset="1" style="stop-color:#ED1E7F" />
							</linearGradient>
					</#assign>
				
					<@facetColor colorAvailable="#ED1E7F" colorHigherAvailable="url(#SVGID_2_)" colorHigherAvailableFragment=higherFragment facet=label.lineageFacet ; color>
				
							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M152.178,152.021l59.795,59.794
			C235.991,187.799,248,156.321,248,124.844l-84.563-0.001C163.437,134.68,159.684,144.517,152.178,152.021z"></path>

							<path fill="none" stroke="#FFFFFF" stroke-width="2" stroke-miterlimit="10" d="M180.26,138.392"></path>
							<path fill-rule="evenodd" clip-rule="evenodd" fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}"
								stroke-miterlimit="10"
								d="
			M198.977,166.95c3.001-0.276,5.433-1.629,7.384-3.96c0.974-1.57,1.49-2.521,1.815-4.25c-1.401-0.94-2.581-1.956-4.189-2.871
			c-0.539-0.307-1.866-0.759-1.787-0.384c-0.142,0.803-0.347,1.723-1.121,2.895c-0.863,1.308-2.346,1.931-3.802,1.758
			c-0.557-0.114-1.122-0.344-1.696-0.73c-3.696-2.486-7.398-4.966-11.098-7.448c-1.875-1.259-2.389-3.685-1.172-5.528
			c0.863-1.308,2.346-1.931,3.801-1.759c0.557,0.114,1.123,0.344,1.697,0.73c2.202,1.481,4.015,2.695,6.219,4.174
			c1.52-1.11,3.024-2.03,5-2.375c1.331-0.233,2.454-0.12,3.62,0.041c-0.698-0.446-3.011-2.064-4.323-2.938
			c-2.435-1.753-4.902-3.548-7.528-5.071c-1.674-0.972-3.457-1.41-5.202-1.41c-0.388-0.008-0.781,0-1.182,0.037
			c-3.001,0.276-5.433,1.629-7.384,3.96c-1.125,1.839-1.747,3.33-2.032,5.333c-0.1,0.702-0.115,1.416-0.169,2.124
			c0.189,0.959,0.714,2.987,0.74,3.059c0.857,2.395,2.461,4.166,4.557,5.554c1.313,0.87,2.625,1.741,3.937,2.614
			c2.436,1.754,4.904,3.549,7.531,5.074c1.674,0.972,3.457,1.41,5.202,1.41C198.183,166.995,198.576,166.986,198.977,166.95z"></path>
							<path fill-rule="evenodd" clip-rule="evenodd" fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}"
								stroke-miterlimit="10"
								d="
			M201.202,147.875c-3.001,0.276-5.433,1.629-7.384,3.96c-0.974,1.57-1.49,2.521-1.815,4.25c1.401,0.94,2.581,1.956,4.189,2.871
			c0.539,0.307,1.866,0.759,1.787,0.384c0.142-0.803,0.347-1.723,1.121-2.895c0.863-1.308,2.346-1.931,3.802-1.758
			c0.557,0.114,1.122,0.344,1.696,0.73c3.696,2.486,7.398,4.966,11.098,7.448c1.875,1.259,2.389,3.685,1.172,5.528
			c-0.863,1.308-2.346,1.931-3.801,1.759c-0.557-0.114-1.123-0.344-1.697-0.73c-2.202-1.481-4.015-2.695-6.219-4.174
			c-1.52,1.11-3.024,2.03-5,2.375c-1.331,0.233-2.454,0.12-3.62-0.041c0.698,0.446,3.011,2.064,4.323,2.938
			c2.435,1.753,4.902,3.548,7.528,5.071c1.674,0.972,3.457,1.41,5.202,1.41c0.388,0.008,0.781,0,1.182-0.037
			c3.001-0.276,5.433-1.629,7.384-3.96c1.125-1.839,1.747-3.33,2.032-5.333c0.1-0.702,0.115-1.416,0.169-2.124
			c-0.189-0.959-0.714-2.987-0.74-3.059c-0.857-2.395-2.461-4.166-4.557-5.554c-1.313-0.87-2.625-1.741-3.937-2.614
			c-2.436-1.754-4.904-3.549-7.531-5.074c-1.674-0.972-3.457-1.41-5.202-1.41C201.996,147.829,201.603,147.838,201.202,147.875z"></path>
						
					</@facetColor>
				</a>
			</g>

			<!-- Producer Comments -->
			<g class="producer_comments" id="producer_comments">
				<title>${label.producerCommentsFacet.title!"NA"}</title><#lt>
				<a xlink:href="${label.producerCommentsFacet.href!"NA"}" target="_blank">

					<#assign higherFragment>
							<linearGradient id="SVGID_3_" gradientUnits="userSpaceOnUse" x1="238.5967" y1="77.3408" x2="158.6666"
								y2="114.6128">
								<stop offset="0.3" style="stop-color:#FFFFFF" />
								<stop offset="0.3417" style="stop-color:#FCD8E9" />
								<stop offset="0.3956" style="stop-color:#F8ADD0" />
								<stop offset="0.4537" style="stop-color:#F586BA" />
								<stop offset="0.5153" style="stop-color:#F366A8" />
								<stop offset="0.5815" style="stop-color:#F14C99" />
								<stop offset="0.6539" style="stop-color:#EF378D" />
								<stop offset="0.7357" style="stop-color:#EE2985" />
								<stop offset="0.835" style="stop-color:#ED2180" />
								<stop offset="1" style="stop-color:#ED1E7F" />
							</linearGradient>
					</#assign>
					
					<@facetColor colorAvailable="#ED1E7F" colorHigherAvailable="url(#SVGID_3_)" colorHigherAvailableFragment=higherFragment
						facet=label.producerCommentsFacet ; color>
												
							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M163.437,124.843H248
			c0-33.965-13.768-64.716-36.026-86.974l-59.795,59.794C159.135,104.619,163.437,114.229,163.437,124.843z"></path>

							<path fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" stroke-width="${strokeWidth}"
								d="M188.653,89.253c1.945-0.302,5.039,1.167,5.039,1.167
			c-6.073,6.13-14.53,14.059-18.557,17.812c-1.409-0.141-3.309-0.82-3.358-2.119c-0.056-1.48,2.205-3.086,3.888-4.739
			c3.445-3.38,5.113-5.168,8.452-8.515C185.815,91.159,187.318,89.458,188.653,89.253z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" stroke-width="${strokeWidth}"
								d="M199.762,84.364c1.537,1.424,1.94,1.931,3.423,3.345
			c-6.245,6.253-18.702,18.405-20.879,20.428c-2.453,2.179-4.681,4.047-4.681,4.047l-6.313,2.901l3.385-6.281
			c0,0,3.415-3.363,5.421-5.298C186.669,97.192,193.528,90.447,199.762,84.364z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" stroke-width="${strokeWidth}"
								d="M212.824,102.18l-1.007,9.414l-21.402-0.117
			c-4.201,0.109-8.396,0.829-8.046-3.098c0.181-2.039,0.69-3.121,4.314-3.321h7.009c0,0-2.244-1.6-4.778-3.176
			c1.483-1.555,7.1-6.947,9.309-9.12C199.533,93.493,212.824,102.18,212.824,102.18z"></path>

							<line fill="none" stroke="#000000" stroke-miterlimit="10" x1="191.161" y1="105.085" x2="196.407" y2="105.085"></line>
							<path fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" stroke-width="${strokeWidth}"
								d="M219.269,72.897c-3.805,0-7.246,0.454-9.912,1.958
			c-2.04,1.151-4.286,3.034-4.286,6.494c0.001,5.772,4.373,6.634,7.295,7.61c-1.253,2.804-2.225,3.635-3.487,5.02
			c5.856-0.267,10-3.14,10.791-4.208c8.587-0.133,13.083-3.408,13.077-8.111C232.741,76.221,226.722,72.897,219.269,72.897z"></path>

						
					</@facetColor>
				</a>
			</g>


			<!-- Standards Compliance -->
			<g class="standards_compliance" id="standards_compliance">

				<title>${label.standardsComplianceFacet.title}!"NA"</title><#lt>
				<a xlink:href="${label.standardsComplianceFacet.href!"NA"}" target="_blank">

					<#assign higherFragment>
						<linearGradient id="SVGID_4_" gradientUnits="userSpaceOnUse" x1="171.7383" y1="238.7607" x2="141.5727"
								y2="155.8815">
								<stop offset="0.3" style="stop-color:#FFFFFF" />
								<stop offset="0.3037" style="stop-color:#FBFDFC" />
								<stop offset="0.354" style="stop-color:#CAE9D7" />
								<stop offset="0.4079" style="stop-color:#9ED6B5" />
								<stop offset="0.4649" style="stop-color:#77C698" />
								<stop offset="0.5252" style="stop-color:#57B97F" />
								<stop offset="0.5901" style="stop-color:#3DAE6C" />
								<stop offset="0.661" style="stop-color:#28A65C" />
								<stop offset="0.7412" style="stop-color:#1AA051" />
								<stop offset="0.8384" style="stop-color:#129C4B" />
								<stop offset="1" style="stop-color:#0F9B49" />
							</linearGradient>
					</#assign>

					<@facetColor colorAvailable="#0F9B49" colorHigherAvailable="url(#SVGID_4_)" colorHigherAvailableFragment=higherFragment
						facet=label.standardsComplianceFacet ; color>
				
							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M125,163.28v84.563
			c33.965,0,64.715-13.768,86.975-36.027l-59.796-59.795C145.223,158.977,135.614,163.28,125,163.28z"></path>

							<path fill="none"
								d="M151.773,187.599c-8.837,0-16,7.164-16,16s7.163,16,16,16s16-7.164,16-16S160.61,187.599,151.773,187.599z
			 M151.773,215.599c-6.627,0-12-5.372-12-12s5.373-12,12-12s12,5.372,12,12S158.4,215.599,151.773,215.599z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10"
								d="M151.773,183.599c-11.046,0-20,8.954-20,20
			s8.954,20,20,20s20-8.954,20-20S162.819,183.599,151.773,183.599z M151.773,219.599c-8.837,0-16-7.164-16-16s7.163-16,16-16
			s16,7.164,16,16S160.61,219.599,151.773,219.599z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10"
								d="M151.773,191.599c-6.627,0-12,5.372-12,12
			s5.373,12,12,12s12-5.372,12-12S158.4,191.599,151.773,191.599z M151.773,211.599c-4.418,0-8-3.582-8-8s3.582-8,8-8s8,3.582,8,8
			S156.191,211.599,151.773,211.599z"></path>
							<circle fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10" cx="151.773"
								cy="203.599" r="4"></circle>

							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10"
								d="M187.716,193.992
			c-0.162-0.685-0.645-1.215-1.357-1.493c-1.251-0.488-2.524-0.186-3.466,0.825l-11.276,12.12c-3.174,3.411-6.348,6.822-9.495,10.219
			c-0.342-0.174-0.686-0.342-1.022-0.506l-1.692-0.834c-1.663-0.823-3.326-1.645-4.996-2.463c-0.292-0.141-0.619-0.202-1.024-0.155
			l-1.617,0.004l0.141,1.729c0.003,0.189,0.01,0.583,0.215,0.992c2.218,4.433,4.443,8.866,6.675,13.291
			c0.26,0.517,0.536,1.026,0.803,1.514l0.383,0.707c0.604,1.127,1.423,1.389,1.975,1.409l0.091,0.004l0.046-0.003
			c1.297-0.053,1.846-1.104,2.081-1.553c1.494-2.867,3.301-5.905,5.684-9.565c3.913-6.008,8.057-11.75,13.075-18.521
			c0.917-1.238,1.842-2.471,2.768-3.701l1.521-2.026C187.702,195.357,187.873,194.648,187.716,193.992z"></path>
					
					</@facetColor>
				</a>
			</g>


			<!-- Quality Information -->
			<g class="quality_information" id="quality_information">

				<title>${label.qualityInformationFacet.title!"NA"}</title><#lt>
				<a xlink:href="${label.qualityInformationFacet.href!"NA"}" target="_blank">

					<#assign higherFragment>
						<linearGradient id="SVGID_5_" gradientUnits="userSpaceOnUse" x1="77.8032" y1="238.4434" x2="115.0769"
								y2="158.5097">
								<stop offset="0.3" style="stop-color:#FFFFFF" />
								<stop offset="0.3037" style="stop-color:#FBFDFC" />
								<stop offset="0.354" style="stop-color:#CAE9D7" />
								<stop offset="0.4079" style="stop-color:#9ED6B5" />
								<stop offset="0.4649" style="stop-color:#77C698" />
								<stop offset="0.5252" style="stop-color:#57B97F" />
								<stop offset="0.5901" style="stop-color:#3DAE6C" />
								<stop offset="0.661" style="stop-color:#28A65C" />
								<stop offset="0.7412" style="stop-color:#1AA051" />
								<stop offset="0.8384" style="stop-color:#129C4B" />
								<stop offset="1" style="stop-color:#0F9B49" />
							</linearGradient>
					</#assign>

					<@facetColor colorAvailable="#0F9B49" colorHigherAvailable="url(#SVGID_5_)" colorHigherAvailableFragment=higherFragment
						facet=label.qualityInformationFacet ; color>
					
							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M98.128,152.021l-59.795,59.794
			c24.018,24.019,55.496,36.027,86.974,36.027V163.28C115.47,163.28,105.633,159.527,98.128,152.021z"></path>

							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10"
								d="M107.311,209.518l-0.383,0.868
			c-0.381,0.875-1.188,2.7-1.373,3.071c-0.662,1.331-1.5,2.022-2.719,2.241c-0.543,0.097-7.227,0.199-12.391,0.244l10.899-13.165
			l-9.778-12.225l0.901-0.001c7.696,0,8.609,0.145,8.687,0.161c1.101,0.234,1.863,0.617,2.39,1.191
			c0.212,0.275,0.869,2.236,1.431,4.447l0.212,0.844h4.382l-1.021-12.311H79.251l0.076,1.189c0.074,1.107,0.272,2.563,0.797,3.214
			c2.51,3.103,10.165,12.648,12.326,15.344l-0.264,0.314c-2.605,3.1-9.064,10.784-11.98,14.224c-0.729,0.863-0.795,2.753-0.79,3.526
			l0.007,1.115l29.674-0.162l2.155-13.123L107.311,209.518z"></path>
					
					</@facetColor>
				</a>
			</g>

			<!-- User Feedback -->
			<g class="user_feedback" id="user_feedback">
				<title>${label.userFeedbackFacet.title!"NA"}</title><#lt>
				<a xlink:href="${label.userFeedbackFacet.href!"NA"}" target="_blank">

					<#assign higherFragment>	
						<linearGradient id="SVGID_6_" gradientUnits="userSpaceOnUse" x1="11.4023" y1="172.3452" x2="91.332"
								y2="135.0734">
								<stop offset="0.3" style="stop-color:#FFFFFF" />
								<stop offset="0.3397" style="stop-color:#FDEADA" />
								<stop offset="0.3938" style="stop-color:#FBD2AF" />
								<stop offset="0.4521" style="stop-color:#FABC88" />
								<stop offset="0.5139" style="stop-color:#F8AA68" />
								<stop offset="0.5803" style="stop-color:#F79C4E" />
								<stop offset="0.6529" style="stop-color:#F69039" />
								<stop offset="0.735" style="stop-color:#F5882B" />
								<stop offset="0.8345" style="stop-color:#F58323" />
								<stop offset="1" style="stop-color:#F58220" />
							</linearGradient>
					</#assign>

					<@facetColor colorAvailable="#F58220" colorHigherAvailable="url(#SVGID_6_)" colorHigherAvailableFragment=higherFragment
						facet=label.userFeedbackFacet ; color>

							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M86.563,124.844H2
					c0,33.964,13.767,64.714,36.025,86.972l59.796-59.794C90.864,145.066,86.563,135.457,86.563,124.844z"></path>

							<circle fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10" cx="39.282"
								cy="153.606" r="8.613"></circle>
							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10"
								d="M26.364,185.04c0,0,0.802-6.583,1.265-8.721
				c0.417-1.933,0.466-4.443,1.951-7.488c1.427-2.923,3.765-5.322,3.765-5.322c1.961,1.415,9.282,1.596,11.875,0
				c0,0,2.357,2.567,3.738,5.254c1.46,2.842,1.559,5.622,1.978,7.555c0.463,2.138,1.265,8.721,1.265,8.721H26.364z"></path>
							<path fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10"
								d="M63.206,134.761
			c-3.804,0-7.245,0.455-9.911,1.959c-2.04,1.152-4.286,3.034-4.286,6.494c0,5.772,4.372,6.633,7.295,7.609
			c-1.254,2.804-2.225,3.636-3.488,5.021c5.856-0.267,10-3.14,10.792-4.208c8.587-0.132,13.082-3.409,13.076-8.111
			C76.678,138.085,70.659,134.761,63.206,134.761z"></path>
						
					</@facetColor>
				</a>
			</g>

			<!-- Expert Feedback -->
			<g class="expert_review" id="expert_review">
				<title>${label.expertFeedbackFacet.tile!"NA"}</title><#lt>
				<a xlink:href="${label.expertFeedbackFacet.href!"NA"}" target="_blank">
 
 					<#assign higherFragment>
 						<linearGradient id="SVGID_7_" gradientUnits="userSpaceOnUse" x1="11.3984" y1="77.3389" x2="91.3332"
								y2="114.6131">
								<stop offset="0.3" style="stop-color:#FFFFFF" />
								<stop offset="0.3034" style="stop-color:#FCFDFE" />
								<stop offset="0.3606" style="stop-color:#D1DDEE" />
								<stop offset="0.4212" style="stop-color:#ABC1E0" />
								<stop offset="0.4865" style="stop-color:#8BA9D4" />
								<stop offset="0.5566" style="stop-color:#7196CA" />
								<stop offset="0.6333" style="stop-color:#5C87C2" />
								<stop offset="0.72" style="stop-color:#4E7CBD" />
								<stop offset="0.8252" style="stop-color:#4676BA" />
								<stop offset="1" style="stop-color:#4374B9" />
							</linearGradient>
					</#assign>
 
					<@facetColor colorAvailable="#4374B9" colorHigherAvailable="url(#SVGID_7_)" colorHigherAvailableFragment=higherFragment
						facet=label.expertFeedbackFacet ; color>
						
							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M97.822,97.664L38.025,37.869
					C14.008,61.886,2,93.365,2,124.843h84.563C86.563,115.005,90.316,105.169,97.822,97.664z"></path>

							<rect x="23.24" y="72.558" fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10"
								width="35.208" height="42.866"></rect>
							<line fill="none" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10" x1="27.649" y1="82.002"
								x2="52.908" y2="82.002"></line>
							<line fill="none" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10" x1="27.649" y1="89.924"
								x2="52.908" y2="89.924"></line>
							<line fill="none" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10" x1="27.881" y1="97.53"
								x2="53.141" y2="97.53"></line>
							<line fill="none" stroke="#000000" stroke-width="${strokeWidth*0.75}" stroke-miterlimit="10" x1="27.649" y1="105.914"
								x2="52.908" y2="105.914"></line>
							<path fill="none" stroke="#000000" stroke-width="2" stroke-miterlimit="10" d="M53.752,61.481"></path>
							<rect x="49.176" y="102.612" transform="matrix(0.8492 0.5281 -0.5281 0.8492 65.179 -16.148)" fill="#FFFFFF"
								stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10" width="23.373" height="6.867"></rect>
							<circle fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10" cx="42.84"
								cy="94.266" r="11.393"></circle>
							<circle fill="#FFFFFF" stroke="#000000" stroke-width="${strokeWidth}" stroke-miterlimit="10" cx="42.84"
								cy="94.265" r="8.584"></circle>
			
					</@facetColor>
				</a>
			</g>

			<!-- Citation Information -->
			<g class="citations_information" id="citations_information">
				<title>${label.citationsFacet.title!"NA"}</title><#lt>
				<a xlink:href="${label.citationsFacet.href!"NA"}" target="_blank">

					<#assign higherFragment>
						<linearGradient id="SVGID_8_" gradientUnits="userSpaceOnUse" x1="77.498" y1="11.4033" x2="114.7702"
								y2="91.3336">
								<stop offset="0.3" style="stop-color:#FFFFFF" />
								<stop offset="0.3034" style="stop-color:#FCFDFE" />
								<stop offset="0.3606" style="stop-color:#D1DDEE" />
								<stop offset="0.4212" style="stop-color:#ABC1E0" />
								<stop offset="0.4865" style="stop-color:#8BA9D4" />
								<stop offset="0.5566" style="stop-color:#7196CA" />
								<stop offset="0.6333" style="stop-color:#5C87C2" />
								<stop offset="0.72" style="stop-color:#4E7CBD" />
								<stop offset="0.8252" style="stop-color:#4676BA" />
								<stop offset="1" style="stop-color:#4374B9" />
							</linearGradient>
					</#assign>

					<@facetColor colorAvailable="#4374B9" colorHigherAvailable="url(#SVGID_8_)" colorHigherAvailableFragment=higherFragment
						facet=label.citationsFacet ; color>
			
							<path fill="${color}" stroke="#000000" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10"
								d="M125,86.563V2
					C91.035,2,60.285,15.768,38.025,38.026l59.796,59.795C104.777,90.865,114.386,86.563,125,86.563z"></path>

							<path fill="#FFFFFF"
								d="M86.79,24.714L75.591,56.886c5.157,3.054,9.518,8.798,9.596,8.926l11.452-31.27
			C95.097,29.475,90.677,26.012,86.79,24.714z"></path>
							<path fill="#FFFFFF"
								d="M94.73,65.396c2.31,0,5.556,0.446,5.662,0.466l11.737-31.821c-2.068-1.111-7.352-1.74-7.388-1.738
			c-2.786,0.075-5.224,0.994-6.909,2.608L86.34,66.293l-0.035,0.092c2.635-0.647,5.427-0.979,8.311-0.987
			C94.654,65.398,94.692,65.396,94.73,65.396z"></path>
							<path fill="#FFFFFF"
								d="M74.645,57.775l-3.218-0.224l0.044,0.03l11.189,7.21l0.671,0.433
			C81.372,62.314,79.401,60.566,74.645,57.775z"></path>
							<polygon fill="#FFFFFF" points="85.511,24.602 80.932,24.809 69.908,56.197 74.4,56.506 	"></polygon>
							<path fill="#FFFFFF"
								d="M94.663,66.638c-0.034,0-0.068,0.002-0.101,0.003c-1.989,0.005-3.946,0.175-5.847,0.51l14.35,2.246
			l-2.557-2.239C99.859,67.026,97.667,66.638,94.663,66.638z"></path>
							<polygon fill="#FFFFFF" points="104.827,69.282 116.6,37.59 113.182,34.797 101.527,66.394 	"></polygon>
							<path fill="none"
								d="M86.79,24.714L75.591,56.886c5.157,3.054,9.518,8.798,9.596,8.926l11.452-31.27
			C95.097,29.475,90.677,26.012,86.79,24.714z"></path>
							<path fill="none"
								d="M94.73,65.396c2.31,0,5.556,0.446,5.662,0.466l11.737-31.821c-2.068-1.111-7.352-1.74-7.388-1.738
			c-2.786,0.075-5.224,0.994-6.909,2.608L86.34,66.293l-0.035,0.092c2.635-0.647,5.427-0.979,8.311-0.987
			C94.654,65.398,94.692,65.396,94.73,65.396z"></path>
							<path fill="none"
								d="M74.645,57.775l-3.218-0.224l0.044,0.03l11.189,7.21l0.671,0.433C81.372,62.314,79.401,60.566,74.645,57.775z
			"></path>
							<polygon fill="none" points="85.511,24.602 80.932,24.809 69.908,56.197 74.4,56.506 		"></polygon>
							<path fill="none"
								d="M94.663,66.638c-0.034,0-0.068,0.002-0.101,0.003c-1.989,0.005-3.946,0.175-5.847,0.51l14.35,2.246
			l-2.557-2.239C99.859,67.026,97.667,66.638,94.663,66.638z"></path>
							<polygon fill="none" points="113.182,34.797 101.527,66.394 104.827,69.282 116.6,37.59 		"></polygon>
							<path
								d="M112.726,33.128c0,0-5-1.833-7.611-1.833c-3.021,0-5.705,0.914-7.634,2.583c-1.777-5.302-6.495-8.89-10.589-10.186
			l-6.682,0.144L68.591,56.918l15.98,10.552l1.032,0.307l19.882,2.925l12.306-33.434L112.726,33.128z M80.932,24.809l4.58-0.207
			L74.4,56.506l-4.493-0.309L80.932,24.809z M82.66,64.791l-11.189-7.21l-0.044-0.03l3.218,0.224
			c4.757,2.791,6.728,4.54,8.687,7.449L82.66,64.791z M85.188,65.813c-0.079-0.128-4.439-5.873-9.596-8.926L86.79,24.714
			c3.887,1.298,8.307,4.761,9.849,9.829L85.188,65.813z M86.34,66.293l11.493-31.381c1.685-1.615,4.123-2.533,6.909-2.608
			c0.036-0.002,5.319,0.627,7.388,1.738l-11.737,31.821c-0.106-0.021-3.352-0.466-5.662-0.466c-0.038,0-0.077,0.001-0.114,0.001
			c-2.884,0.008-5.676,0.34-8.311,0.987L86.34,66.293z M88.715,67.15c1.901-0.334,3.858-0.505,5.847-0.51
			c0.033,0,0.067-0.003,0.101-0.003c3.005,0,5.197,0.389,5.845,0.52l2.557,2.239L88.715,67.15z M104.827,69.282l-3.3-2.888
			l11.655-31.597l3.418,2.793L104.827,69.282z"></path>

							<path fill="none" stroke="#000000" stroke-width="2" stroke-miterlimit="10" d="M53.752,61.481"></path>
					
					</@facetColor>
				</a>
			</g>
		</g>


		<!-- Branding Group -->
		<g class="branding_group" id="branding_group"> <!-- Branding -->
			<g class="branding" id="branding">
				<a xlink:href="http://www.geoviqua.org" xlink:title="Visit GeoViQua website" target="_blank">
					<circle fill="#FFFFFF" stroke="#010101" stroke-width="${outerStrokeWidth}" stroke-miterlimit="10" cx="125.277"
						cy="124.996" r="38.437"></circle>
					<path fill="#324E9C"
						d="M125.916,109.439c2.668-0.264,5.231-0.307,9.245,1.796c0,1.933,0,3.865,0,5.797
						c-1.369-0.791-2.898-1.576-4.831-2.21c-1.233-0.359-2.406-0.349-4.136-0.134c-3.008,0.312-5.577,2.172-7.041,4.141
						c-0.55,0.74-1.397,2.952-1.481,3.737c3.175,0,6.726-0.009,9.899-0.009c0.154,1.494,0.052,3.445,0.138,5.109
						c-3.313,0.089-6.737,0.236-9.83,0.145c0.582,1.575,1.446,6.615,7.896,7.725c1.381,0.202,2.385,0.229,4.002,0
						c2.361-0.171,4.006-1.793,5.382-2.486c0,2.025,0,3.775,0,5.798c-1.876,0.887-3.932,2.072-8.003,2.072
						c-6.18-0.533-10.35-3.499-12.97-7.595c1.529-2.79,3.383-6.091,2.696-10.518c-1.154-0.034-4.688,0.174-4.688-0.058
						c0.172-0.719,0.232-1.163,0.473-2.266C114.366,114.754,118.991,110.609,125.916,109.439z"></path>
					<path fill="#057E67"
						d="M147.857,138.161c-6.26,0.323-9.772-2.096-12.28-5.524c1.146-1.616,2.29-3.232,2.759-5.524
						c1.405,3.672,4.125,6.397,8.555,6.077c4.194-0.303,7.484-3.642,7.449-8.147c-0.068-9.666-14.354-10.389-15.866-1.795
						c-0.702-2.106-1.589-4.025-2.897-5.525c2.547-3.337,5.729-5.914,11.314-5.661c7.209,0.333,12.946,6.724,12.42,14.085
						C158.847,132.609,154.705,137.361,147.857,138.161z"></path>
					<path fill="#246F7E"
						d="M114.188,133.326c-2.029,2.252-4.652,3.908-8.141,4.695c-0.092,0-0.186,0-0.277,0
						c-1.695,0.228-4.05,0.136-5.934-0.554c-4.131-1.709-7.015-4.667-8.417-9.112c0-0.047,0-0.091,0-0.137
						c-0.767-3.22-0.126-7.266,0.965-8.976c2.676-4.245,6.749-7.76,14.076-6.904c0.045,0,0.879,0.192,1.24,0.246
						c0,1.702-0.137,3.438-0.137,5.138c-1.694-0.789-4.73-0.923-6.486-0.137c-2.945,1.198-5.099,3.643-5.243,7.181
						c-0.219,5.25,4.253,9.377,9.796,8.284c0.047,0,0.094,0,0.138,0c3.129-0.548,4.947-3.107,6.024-5.713
						c-2.944,0.063-6.758,0.156-10.163,0.054c0-1.52,0-3.038,0-4.558c3.404-0.089,13.874-0.119,15.253-0.026
						C118.21,126.969,114.189,133.603,114.188,133.326z"></path>
				</a>
			</g>
		</g>
		
		<!-- Error Group -->
		<g class="error_group" id="error_group" display="<#if label.errorFacet.availability.name() = "AVAILABLE">inline<#else>none</#if>">
			<g class="error" id="error" transform="matrix(0.06021327,0,0,0.06021327,211.00551,215.92913)">
				<!-- public domain image used: http://upload.wikimedia.org/wikipedia/commons/d/dd/Achtung.svg -->
				<a xlink:title="${(label.errorFacet.errorMessage)!"No Error."}" target="_blank">
					<path
		             id="path2231"
		             d="M 614.57,504.94 335.17,21 C 330.79,13.412 322.7,8.738 313.94,8.738 c -8.76,0 -16.85,4.674 -21.23,12.258 L 13.3,504.936 c -4.375,7.58 -4.375,16.93 0.003,24.52 4.379,7.58 12.472,12.25 21.23,12.25 h 558.81 c 8.76,0 16.86,-4.67 21.23,-12.25 4.38,-7.59 4.38,-16.94 0,-24.52 z"
		             inkscape:connector-curvature="0"
		             style="fill:#ea0000" />
		          <polygon
		             id="polygon2233"
		             points="313.94,101.89 93.977,482.88 533.9,482.88 "
		             style="fill:#ffffff" />
		          <path
		             id="path2235"
		             d="m 291.87,343.36 c 1.21,11.49 3.21,20.04 6.02,25.66 2.81,5.63 7.82,8.43 15.04,8.43 h 2.01 c 7.22,0 12.24,-2.8 15.04,-8.43 2.81,-5.62 4.82,-14.17 6.02,-25.66 l 6.42,-88.75 c 1.21,-17.3 1.81,-29.71 1.81,-37.25 0,-10.25 -2.91,-18.25 -8.73,-23.99 -5.53,-5.46 -13.38,-8.59 -21.56,-8.59 -8.18,0 -16.04,3.13 -21.57,8.59 -5.81,5.74 -8.72,13.74 -8.72,23.99 0,7.54 0.6,19.95 1.8,37.25 l 6.42,88.75 z"
		             inkscape:connector-curvature="0" />
		          <circle
		             id="circle2237"
		             cy="430.79001"
		             cx="313.94"
		             r="30.747"
		             d="m 344.687,430.79001 c 0,16.9811 -13.7659,30.747 -30.747,30.747 -16.9811,0 -30.747,-13.7659 -30.747,-30.747 0,-16.9811 13.7659,-30.747 30.747,-30.747 16.9811,0 30.747,13.7659 30.747,30.747 z"
		             sodipodi:cx="313.94"
		             sodipodi:cy="430.79001"
		             sodipodi:rx="30.747"
		             sodipodi:ry="30.747" />
				</a>
			</g>
		</g>
	</g>
	
	</#escape>  
	
	</svg>