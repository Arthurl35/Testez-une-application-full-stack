import { LoginComponent } from './login.component';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  // Configuration des tests avant chaque test
  beforeEach(() => {
    authService = { login: jest.fn() } as any;
    router = { navigate: jest.fn() } as any;
    sessionService = { logIn: jest.fn() } as any;

    // Configuration du module de test
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
        { provide: SessionService, useValue: sessionService },
      ],
    });

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Teste si le formulaire de connexion est bien rendu
  it('should render login form', () => {
    fixture.detectChanges();
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]'));
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]'));
    expect(emailInput).toBeTruthy();
    expect(passwordInput).toBeTruthy();
  });

  // Teste si la méthode submit() appelle la méthode login() du service AuthService avec les bonnes valeurs
  it('should call login method on form submit', () => {
    const loginResponse = { token: 'token' };
    (authService.login as jest.Mock).mockReturnValue(of(loginResponse));
    component.form.controls['email'].setValue('arthur@levesque.com');
    component.form.controls['password'].setValue('test!1234');
    fixture.detectChanges();
    component.submit();
    expect(authService.login).toHaveBeenCalledWith({ email: 'arthur@levesque.com', password: 'test!1234' });
    expect(sessionService.logIn).toHaveBeenCalledWith(loginResponse);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  // Teste si la méthode submit() navigue vers la page des sessions et appelle la méthode logIn() du service SessionService lors d'une connexion réussie
  it('should set onError to true when login fails', () => {
    (authService.login as jest.Mock).mockReturnValue(throwError('error')); // Simule une erreur
    component.form.controls['email'].setValue('arthur@levesque.com');      // Rempli le formulaire
    component.form.controls['password'].setValue('test!1234');             
    fixture.detectChanges();                                               // Rafraichit le composant                     
    component.submit();                                                    // Appelle la méthode submit              
    expect(component.onError).toBe(true);                                  // Vérifie que onError est bien à true        
  });
});