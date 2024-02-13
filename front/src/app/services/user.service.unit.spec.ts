import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  // Avant chaque test, on configure le module de test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Importer le module HttpClientTestingModule pour pouvoir tester les requêtes HTTP
      providers: [UserService]            // Fournir le service à tester
    });

    //Créer une instance du service et du HttpTestingController
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // Après chaque test, on vérifie qu'il n'y a pas de requêtes HTTP en attente
  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test pour vérifier que le service récupère bien l'utilisateur par son id
  it('should get user by id', () => {
    const mockUser: User = {
      id: 1,
      email: 'arthur@levesque.com',
      lastName: 'Arthur',
      firstName: 'levesque',
      admin: false,
      password: 'test!1234',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser); 
    });

    const req = httpMock.expectOne('api/user/1'); 
    expect(req.request.method).toBe('GET');
    req.flush(mockUser); 
  }); 

  // Test pour vérifier que le service supprime bien l'utilisateur par son id
  it('should delete user by id', () => {
    service.delete('1').subscribe(response => {
      expect(response).toEqual({});
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});