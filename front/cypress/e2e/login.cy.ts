describe('Login spec', () => {
  it('Login successful', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', []).as('session');

    cy.get('input[formControlName=email]').type("arthur@levesque.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.wait(1000);
    cy.url().should('include', '/sessions');

    cy.wait(1000);
    cy.contains('Logout').click();
    cy.url().should('include', '/');
  });

  it('Login failure', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        error: 'Invalid credentials'
      },
    });

    cy.get('input[formControlName=email]').type("arthur@levesque.com");
    cy.get('input[formControlName=password]').type(`${"aaaaaaaaaaaaaaaaaaa!1234"}{enter}{enter}`);

    cy.get('.error').should('be.visible');
  });
});
