<h1>HDFC Life Policy Ledger</h1>
 
<h2>1. How to Run</h2>
 
<h3>Prerequisites</h3>
<ul>
    <li>Java 17</li>
    <li>Maven 3.8+</li>
</ul>
 
<h3>Run</h3>
 
<pre><code>mvn spring-boot:run</code></pre>
 
<p>or</p>
 
<pre><code>./mvnw spring-boot:run</code></pre>
 
<p>Default profile: <code>dev</code></p>
 
<p>Development database:</p>
 
<pre><code>jdbc:h2:mem:hdfclife;MODE=PostgreSQL;DB_CLOSE_DELAY=-1</code></pre>
 
<p>Swagger UI:</p>
 
<pre><code>http://localhost:8080/swagger-ui/index.html</code></pre>
 
<hr>
 
<h2>2. Endpoint Table</h2>
 
<table>
    <thead>
        <tr>
            <th>Method</th>
            <th>Endpoint</th>
            <th>Description</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>GET</td>
            <td><code>/api/policies</code></td>
            <td>Get all policies</td>
            <td>200</td>
        </tr>
        <tr>
            <td>GET</td>
            <td><code>/api/policies/{policyNo}</code></td>
            <td>Get policy by policy number</td>
            <td>200, 404</td>
        </tr>
        <tr>
            <td>GET</td>
            <td><code>/api/policies?status=Active</code></td>
            <td>Filter by status</td>
            <td>200</td>
        </tr>
        <tr>
            <td>GET</td>
            <td><code>/api/policies?type=TERM</code></td>
            <td>Filter by policy type</td>
            <td>200</td>
        </tr>
        <tr>
            <td>GET</td>
            <td><code>/api/policies?customer=Anita%20Sharma</code></td>
            <td>Filter by customer</td>
            <td>200</td>
        </tr>
        <tr>
            <td>GET</td>
            <td><code>/api/policies/search?minPremium=20000</code></td>
            <td>Search by minimum premium</td>
            <td>200</td>
        </tr>
        <tr>
            <td>POST</td>
            <td><code>/api/policies</code></td>
            <td>Create policy</td>
            <td>201, 400, 409</td>
        </tr>
        <tr>
            <td>DELETE</td>
            <td><code>/api/policies/{policyNo}</code></td>
            <td>Delete policy</td>
            <td>204, 404</td>
        </tr>
        <tr>
            <td>GET</td>
            <td><code>/api/policies/{policyNo}/claims</code></td>
            <td>Get claims for a policy</td>
            <td>200, 404</td>
        </tr>
        <tr>
            <td>POST</td>
            <td><code>/api/claims</code></td>
            <td>Create claim</td>
            <td>201, 400, 404</td>
        </tr>
        <tr>
            <td>GET</td>
            <td><code>/api/claims/{claimNo}</code></td>
            <td>Get claim by claim number</td>
            <td>200, 404</td>
        </tr>
    </tbody>
</table>
 
<hr>
 
<h2>3. Which Side Owns <code>policy_riders</code>?</h2>
 
<p>
    <code>Policy</code> owns the <code>policy_riders</code> relationship.
    The <code>@ManyToMany</code> mapping is defined in <code>Policy</code>
    using <code>@JoinTable</code>, which specifies <code>policy_riders</code>
    as the join table. <code>Rider</code> is the inverse side using
    <code>mappedBy = "riders"</code>. Therefore, changes to the policy-rider
    relationship are managed from the <code>Policy</code> side.
</p>
 
<hr>
 
<h2>4. Why Return DTOs When <code>open-in-view</code> Is False?</h2>
 
<p>
    DTOs allow the API to return only the required data instead of exposing
    JPA entities directly. With <code>open-in-view=false</code>, the Hibernate
    session is closed before the controller serializes the response. If a
    <code>Policy</code> entity contains a lazy <code>riders</code> collection
    and the controller tries to access it after the session is closed,
    Hibernate can throw a <code>LazyInitializationException</code>. Mapping
    the entity to a DTO inside the service transaction ensures the required
    data is loaded safely before the response is returned.
</p>
 
