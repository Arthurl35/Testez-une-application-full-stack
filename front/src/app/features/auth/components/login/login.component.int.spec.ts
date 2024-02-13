import { TestBed, ComponentFixture } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';

describe('LoginComponent Integration Test', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(() => {
    // Configuration du module de test
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, HttpClientTestingModule, RouterTestingModule],
      declarations: [LoginComponent],
      providers: [AuthService, SessionService]
    });

    // Création du composant et injection des dépendances
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  // Définition d'un objet de session fictif pour les tests
  const mockSessionInformation: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'username',
    firstName: 'Arthur',
    lastName: 'levesque',
    admin: false
  };

  // Teste si la méthode submit() appelle la méthode login() du service AuthService avec les bonnes valeurs
  it('should call AuthService.login on submit', () => {
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation));
    component.form.setValue({ email: 'arthur@levesque.com', password: 'test!1234' });
    component.submit();
    expect(loginSpy).toHaveBeenCalledWith({ email: 'arthur@levesque.com', password: 'test!1234' });
  });
  
  // Teste si la méthode submit() navigue vers la page des sessions et appelle la méthode logIn() du service SessionService lors d'une connexion réussie
  it('should navigate to sessions page and call SessionService.logIn on successful login', () => {
    jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation));
    const logInSpy = jest.spyOn(sessionService, 'logIn');
    const navigateSpy = jest.spyOn(router, 'navigate');
    component.form.setValue({ email: 'arthur@levesque.com', password: 'test!1234' });
    component.submit();
    expect(logInSpy).toHaveBeenCalledWith(mockSessionInformation);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });
  
  // Teste si la méthode submit() définit onError à true lors d'une connexion échouée
  it('should set onError to true on failed login', () => {
    jest.spyOn(authService, 'login').mockReturnValue(throwError('error'));
    component.form.setValue({ email: 'arthur@levesque.com', password: 'test!1234' });
    component.submit();
    expect(component.onError).toBe(true);
  });
});