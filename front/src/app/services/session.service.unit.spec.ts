import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  let user: SessionInformation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
    user = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'username',
      firstName: 'Arthur',
      lastName: 'levesque',
      admin: false
    };
  });

  // Test pour vérifier que le service est correctement créé
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test pour vérifier que l'utilisateur peut se connecter
  it('should log in a user', () => {
    service.logIn(user); //Appel de la méthode logIn

    expect(service.isLogged).toBe(true); //Vérification que l'utilisateur est bien connecté
    expect(service.sessionInformation).toEqual(user); //Vérification que les informations de session sont bien celles de l'utilisateur
  });

  //Test pour vérifier que l'utilisateur peut se déconnecter
  it('should log out a user', () => {
    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  //Test pour vérifier que le service émet le statut de connexion
  it('should emit isLogged status', (done) => { //Utilisation de la fonction done pour attendre la fin de l'observable
    service.$isLogged().subscribe(isLogged => { //Souscription à l'observable
      expect(isLogged).toBe(true);              //Vérification que le statut de connexion est bien émis
      done();                                  //Fin de l'observable                        
    });

    service.logIn(user);                       //Appel de la méthode logIn
  });
});
