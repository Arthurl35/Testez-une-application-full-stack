describe('Register spec', () => {
  it('Registration successful', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName="firstName"]').type('Arthur');
    cy.get('input[formControlName="lastName"]').type('LEVESQUE');
    cy.get('input[formControlName="email"]').type('arthur@levesque.com');
    cy.get('input[formControlName="password"]').type('test!1234{enter}{enter}');

    cy.url().should('include', '/login')

    cy.wait(1000);
  })

  it('Registration failure', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        error: 'Registration failed'
      },
    })

    cy.get('input[formControlName="firstName"]').type('AAAA');
    cy.get('input[formControlName="lastName"]').type('LLLLLL');
    cy.get('input[formControlName="email"]').type('AAAA@example');
    cy.get('input[formControlName="password"]').type('test1234444{enter}');

    cy.get('.error').should('be.visible');
  })
});