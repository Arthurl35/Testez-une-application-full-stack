// Import des modules nécessaires pour les tests unitaires
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

// Import des services et composants nécessaires pour les tests unitaires
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { FormComponent } from './form.component';
import { Router } from '@angular/router';
import { of } from 'rxjs';

// Définition des tests unitaires pour le composant FormComponent
describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let matSnackBar: MatSnackBar;

  // Mock du service SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  // Mock du service SessionApiService
  const mockedAPIService = {
    detail: jest.fn((id: string) => {
      return of({
        id: id,
        name: 'Mocked Session',
        description: 'Mocked description',
        date: new Date(),
        teacher_id: 2,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      });
    }),
    create: jest.fn((session: any) => {
      return of({});
    }),
    update: jest.fn((id: string, session: any) => {
      return of({});
    }),
  };

  // Configuration des tests avant chaque test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        // Import des modules nécessaires pour le composant
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: FormComponent },
        ]),
        NoopAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
      ],
      providers: [
        // Injection des services mockés
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockedAPIService },
        MatSnackBar,
      ],
      declarations: [FormComponent],
    }).compileComponents();

    // Création du composant et récupération des instances des services et du composant
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();

    matSnackBar = TestBed.inject(MatSnackBar);
  });

  // Test pour vérifier que le composant est créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier la création d'une session
  it('should creating session', () => {
    jest
      .spyOn(mockedAPIService, 'create')
      .mockReturnValue(of(mockSessionService));
    jest.spyOn(matSnackBar, 'open');

    component.submit();
    expect(matSnackBar.open).toBeCalledWith('Session created !', 'Close', {
      duration: 3000,
    });
  });

  // Test pour vérifier la navigation vers le chemin des sessions lorsque la session n'est pas administrateur
  it('should navigate to sessions path when not admin session', () => {
    jest.spyOn(router, 'navigate');
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    fixture.detectChanges();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  // Test pour vérifier la soumission du formulaire lorsque le mode de mise à jour est désactivé
  it('should submit form when not in update mode', () => {
    component.onUpdate = false;
    component.submit();
    expect(mockedAPIService.create).toHaveBeenCalled();
  });

  // Test pour vérifier l'initialisation du formulaire pour la mise à jour
  it('should initialize form for update', () => {
    jest.spyOn(router, 'url', 'get').mockReturnValue('/update/1');
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
    expect(mockedAPIService.detail).toHaveBeenCalled();
  });

  // Test pour vérifier la soumission du formulaire lorsque le mode de mise à jour est activé
  it('should submit form when in update mode', () => {
    component.onUpdate = true;
    component.submit();
    expect(mockedAPIService.update).toHaveBeenCalled();
  });

  // Test pour vérifier que le bouton de sauvegarde est désactivé lorsque le formulaire de session n'est pas valide
  it('should have the save button disabled while the session form is not valid', () => {
    let inputText = new FormControl('');
    const saveButton = fixture.debugElement.nativeElement.querySelector(
      'button[type="submit"]'
    );
    inputText.setValue('');
    fixture.detectChanges();
    expect(saveButton.disabled).toBe(true);
  });
});
