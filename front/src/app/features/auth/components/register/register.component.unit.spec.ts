import { RegisterComponent } from './register.component';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(() => {
    authService = { register: jest.fn() } as any;
    router = { navigate: jest.fn() } as any;

    // Configuration du module de test
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
      ],
    });

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Teste si le formulaire d'inscription est bien rendu
  it('should render register form', () => {
    fixture.detectChanges();
    const firstNameInput = fixture.debugElement.query(By.css('input[formControlName="firstName"]'));
    const lastNameInput = fixture.debugElement.query(By.css('input[formControlName="lastName"]'));
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]'));
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]'));
    expect(firstNameInput).toBeTruthy();
    expect(lastNameInput).toBeTruthy();
    expect(emailInput).toBeTruthy();
    expect(passwordInput).toBeTruthy();
  });

  // Teste si la méthode submit() appelle la méthode register() du service AuthService avec les bonnes valeurs
  it('should call register method on form submit', () => {
    (authService.register as jest.Mock).mockReturnValue(of(null));
    component.form.controls['firstName'].setValue('Arthur');
    component.form.controls['lastName'].setValue('levesque');
    component.form.controls['email'].setValue('Arthur@LEVESQUE.com');
    component.form.controls['password'].setValue('test!1234');
    fixture.detectChanges();
    component.submit();
    expect(authService.register).toHaveBeenCalledWith({ firstName: 'Arthur', lastName: 'levesque', email: 'Arthur@LEVESQUE.com', password: 'test!1234' });
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  // Teste si la propriété onError est bien mise à true lors d'une erreur d'inscription
  it('should set onError to true when register fails', () => {
    (authService.register as jest.Mock).mockReturnValue(throwError('error')); // Simule une erreur
    component.submit();                                                       // Appelle la méthode submit      
    expect(component.onError).toBe(true);                                     // Vérifie que la propriété onError est bien à true
  });
});