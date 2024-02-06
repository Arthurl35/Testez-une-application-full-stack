describe('Session spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
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

    cy.get('input[formControlName=email]').type("arthur@levesque.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
    cy.wait(1000);
  });

  it('Create sessions', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        }
      ]
    });

    cy.intercept('POST', '/api/session', {
      body: {
        id: 2,
        name: 'Session2',
        date: '2024-02-06T00:00:00.000+00:00',
        description: 'session 2',
        teacher_id: 1
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session1',
          description: 'session 1',
          date: '2024-02-06T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        },
        {
          id: 2,
          name: 'Session2',
          description: 'session 2',
          date: '2024-02-06T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }
      ]
    });

    cy.get('button[routerLink=create]').click();
    cy.get('input[formControlName=name]').type("Session2");
    cy.get('input[formControlName=date]').type("2024-02-06");
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type("session 2");

    cy.get('button[type=submit]').click();

    cy.wait(1000);
  });

  it('Update session successful', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session1',
        description: 'session 1',
        date: '2024-02-06T00:00:00.000+00:00',
        teacher_id: 1,
        users: []
      }
    });

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        }
      ]
    });

    cy.contains('Edit').click();

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'UpdatedSession',
        date: '2024-02-06T00:00:00.000+00:00',
        description: 'Updated session description',
        teacher_id: 1
      },
    });

    cy.url().should('include', '/sessions/update');

    cy.get('input[formControlName=name]').clear().type("UpdatedSession");
    cy.get('input[formControlName=date]').clear().type("2024-02-06");
    cy.get('textarea[formControlName=description]').clear().type("Updated session description");

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'UpdatedSession',
          date: '2024-02-06T00:00:00.000+00:00',
          description: 'Updated session description',
          teacher_id: 1,
          users: []
        }
      ]
    });

    cy.get('button[type=submit]').click();
    cy.contains('Sessions').click();

    cy.wait(1000);
  });

  it('Delete session', () => {
    cy.url().should('include', '/sessions');
   
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'UpdatedSession',
        date: '2024-02-06T00:00:00.000+00:00',
        description: 'Updated session description',
        teacher_id: 1,
        users: []
      }
    });
    
    cy.intercept('GET', '/api/teacher/1', {
      body: {
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        }
    });

    cy.contains('Detail').click();

    cy.intercept('DELETE', '/api/session/1', {
      status: 200
    });

    cy.intercept('GET', '/api/session', {
      body: []
    });

    cy.wait(1000);
    cy.contains('Delete').click();

    cy.url().should('include', '/sessions');
  });
});
