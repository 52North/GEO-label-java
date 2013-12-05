GEO-label-java Integration Tests
================================

Integrations tests in this module depend on external components, i.e. a GEO label API server, and therefore are **not** activated by default.

To run the integration tests only, activate the profile ``integration-test``

``mvn clean verify -Pintegration-test``

To run only the integration tests and no other module, use ``mvn clean verify -Pintegration-test -rf :integration-test``.