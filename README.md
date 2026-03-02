<h1 align="center">📊 Monitor Saúde API</h1>

<p align="center">
  REST API for monitoring external services with Discord alerts.<br>
  API REST para monitoramento de serviços externos com alertas via Discord.
</p>

<p align="center">
  <a href="#en-us">🇺🇸 English</a> |
  <a href="#pt-br">🇧🇷 Português</a>
</p>

<hr>

<!-- ========================== ENGLISH VERSION ========================== -->

<h2 id="en-us">🇺🇸 English Version</h2>

<h3>📌 Overview</h3>

<p>
A backend application built with Java and Spring Boot designed to monitor external services automatically.
It periodically checks registered URLs, stores their status history (UP/DOWN), and sends alerts to Discord when status changes occur.
</p>

<ul>
  <li>✔ Layered architecture (controller → service → repository)</li>
  <li>✔ DTO separation between persistence and API models</li>
  <li>✔ Scheduled automatic monitoring (every 60 seconds)</li>
  <li>✔ Discord webhook integration</li>
  <li>✔ Persistent history using H2 in-memory database</li>
  <li>✔ Clean RESTful endpoints</li>
</ul>

<hr>

<h3>🚀 Automatic Monitoring</h3>

<ul>
  <li>✔ Runs every 60 seconds</li>
  <li>✔ Checks all enabled services</li>
  <li>✔ Stores history for each check</li>
  <li>✔ Sends alerts when status changes</li>
</ul>

<p>
If no service is registered or enabled, the monitoring process (AutoCheck) continues running normally.
In this case, the following message is displayed in the application console/log:
</p>

<pre><code>⚠ No registered or enabled services.</code></pre>

<hr>

<h3>📊 Main Endpoints</h3>

<table>
  <tr><th>Method</th><th>Endpoint</th><th>Description</th></tr>
  <tr><td>GET</td><td>/health</td><td>Checks if API is running</td></tr>
  <tr><td>GET</td><td>/services</td><td>List all services</td></tr>
  <tr><td>POST</td><td>/services</td><td>Create new service</td></tr>
  <tr><td>PUT</td><td>/services/{id}</td><td>Update service</td></tr>
  <tr><td>DELETE</td><td>/services/{id}</td><td>Delete service</td></tr>
  <tr><td>GET</td><td>/services/{id}/history</td><td>Service history</td></tr>
</table>

<hr>

<h3>📥 Running Locally</h3>

<pre><code>git clone https://github.com/your_username/monitor-saude-api.git
cd monitor-saude-api
mvn clean install
mvn spring-boot:run
</code></pre>

Access:
<pre><code>http://localhost:8080/health</code></pre>

<hr>

<h3>📬 Discord Webhook Configuration</h3>

Add to <code>application.properties</code>:

<pre><code>discord.webhook.url=</code></pre>

<p><strong>⚠ Do NOT commit your webhook URL.</strong></p>

<hr>

<h3>🚀 Future Improvements</h3>

<ul>
  <li>Authentication & Authorization</li>
  <li>Monitoring dashboard</li>
  <li>Email/SMS alerts</li>
  <li>Dynamic interval configuration</li>
</ul>

<hr>

<!-- ========================== PORTUGUESE VERSION ========================== -->

<h2 id="pt-br">🇧🇷 Versão em Português</h2>

<h3>📌 Visão Geral</h3>

<p>
Aplicação backend desenvolvida em Java com Spring Boot para monitoramento automático de serviços externos.
Verifica URLs periodicamente, registra histórico de status (UP/DOWN) e envia alertas ao Discord quando há mudanças.
</p>

<ul>
  <li>✔ Arquitetura em camadas (controller → service → repository)</li>
  <li>✔ Uso de DTOs separando modelo de persistência da API</li>
  <li>✔ Monitoramento automático a cada 60 segundos</li>
  <li>✔ Integração com webhook do Discord</li>
  <li>✔ Histórico persistido com banco H2 em memória</li>
  <li>✔ Endpoints REST organizados</li>
</ul>

<hr>

<h3>🚀 Monitoramento Automático</h3>

<ul>
  <li>✔ Executa a cada 60 segundos</li>
  <li>✔ Verifica todos os serviços habilitados</li>
  <li>✔ Armazena histórico de cada verificação</li>
  <li>✔ Envia alertas quando o status muda</li>
</ul>

<p>
Caso nenhum serviço esteja cadastrado ou habilitado, o processo de monitoramento (AutoCheck) continua sendo executado normalmente.
Nesse caso, a seguinte mensagem será exibida no console/log da aplicação:
</p>

<pre><code>⚠ Nenhum serviço cadastrado ou habilitado.</code></pre>

<hr>

<h3>📊 Endpoints Principais</h3>

<table>
  <tr><th>Método</th><th>Endpoint</th><th>Descrição</th></tr>
  <tr><td>GET</td><td>/health</td><td>Verifica se a API está funcionando</td></tr>
  <tr><td>GET</td><td>/services</td><td>Lista todos os serviços</td></tr>
  <tr><td>POST</td><td>/services</td><td>Cria novo serviço</td></tr>
  <tr><td>PUT</td><td>/services/{id}</td><td>Atualiza serviço</td></tr>
  <tr><td>DELETE</td><td>/services/{id}</td><td>Remove serviço</td></tr>
  <tr><td>GET</td><td>/services/{id}/history</td><td>Histórico do serviço</td></tr>
</table>

<hr>

<h3>📥 Executando Localmente</h3>

<pre><code>git clone https://github.com/seu_usuario/monitor-saude-api.git
cd monitor-saude-api
mvn clean install
mvn spring-boot:run
</code></pre>

Acesse por:
<pre><code>http://localhost:8080/health</code></pre>

<hr>

<h3>📬 Configuração do Webhook do Discord</h3>

Adicionar no <code>application.properties</code>:

<pre><code>discord.webhook.url=</code></pre>

<p><strong>⚠ Não comite sua URL de webhook.</strong></p>

<hr>

<h3>🚀 Próximas Melhorias</h3>

<ul>
  <li>Autenticação e autorização</li>
  <li>Dashboard de monitoramento</li>
  <li>Alertas por email/SMS</li>
  <li>Configuração dinâmica de intervalos</li>
</ul>

<hr>

<h2>🧡 Author</h2>

<p><strong>Hadassa</strong> — Java Backend Developer</p>

<p>⭐ If you liked this project, leave a star on GitHub!</p>

<hr>

<h2>📄 License</h2>

<p>This project is licensed under the MIT License.</p>
