# Questions

Here are 2 questions related to the codebase. There's no right or wrong answer - we want to understand your reasoning.

## Question 1: API Specification Approaches

When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded everything directly. 

What are your thoughts on the pros and cons of each approach? Which would you choose and why?

**Answer:**
OpenAPI (Design-First) Approach:
PROS:
*The API spec becomes the single source of truth
*Automatic code generation
*Standardization and consistency 
*Better documentation (out-of-box)
*Easier API governance 
CONS:
*Extra upfront effort
*Generated code limitations
*Slower iteration (initially)
*Sync issues (if poorly managed)

Code-First Approach
PROS:
*Faster development 
*More flexibility 
*Simple for small projects
*Easy debugging 
*Less duplication
CONS:
*Documentation often lags
*Inconsistent APIs
*No automatic client generation 
*Testing and validation gaps

My recommendation: Hybrid Approach 
*Clear API contract before coding
*Reduces mismatch 
*consistent standards 
*Easy client generation 
*Better for large teams 

In enterprise microservices, I prefer contract-first using OpenAPI Specification because it establishes a clear API contract, improves collaboration, and enables automatic code/document generation.



## Question 2: Testing Strategy

Given the need to balance thorough testing with time and resource constraints, how would you prioritize tests for this project? 

Which types of tests (unit, integration, parameterized, etc.) would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**

I would prioritize integration tests first, as they validate real system behavior and give the highest value for API-heavy systems.
Then I'd add targeted unit tests for business logic, followed by contract tests(especially for OpenAPI consistency), Parameterized Tests to increase coverage and a small number of E2E tests for critical flows.

I ensure test coverage remains effective by embedding automated tests in CI/CD, updating tests with every change, analyzing production defects and prioritizing high-risk modules rather than focusing only on percentage-based coverage.
