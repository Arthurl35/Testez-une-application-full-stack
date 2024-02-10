import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let route: ActivatedRoute;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let snackBar: MatSnackBar;

  // Configuration des tests avant chaque test
  beforeEach(async () => {
    // Configuration du module de test
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatSnackBarModule
      ],
      // Configuration des services et composants utilisés dans le composant
      providers: [
        { provide: SessionApiService, useValue: { create: jest.fn(), update: jest.fn(), detail: jest.fn() } },  // Mock du service SessionApiService
        { provide: SessionService, useValue: { sessionInformation: { admin: true } } },
        { provide: TeacherService, useValue: { all: jest.fn() } },
        { provide: MatSnackBar, useValue: { open: jest.fn() } },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: jest.fn() } } } }
      ]
    }).compileComponents();

    // Création du composant à tester
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    sessionApiService = TestBed.inject(SessionApiService);
    sessionService = TestBed.inject(SessionService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    snackBar = TestBed.inject(MatSnackBar);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    component.ngOnInit();
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: ''
    });
  });

  //  Test de la méthode submit
  it('should submit form', () => {
    const session: Session = {
      name: 'Test Session',
      date: new Date('2022-01-01'),
      teacher_id: 1,
      description: 'Test Description',
      users: []
    };
  
    const createSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(session));
    const snackBarSpy = jest.spyOn(snackBar, 'open');
    const routerSpy = jest.spyOn(router, 'navigate');
  
    // Initialisez le formulaire avec les valeurs attendues
    component.sessionForm = new FormGroup({
      name: new FormControl('Test Session'),
      date: new FormControl(new Date('2022-01-01')),
      teacher_id: new FormControl(1),
      description: new FormControl('Test Description'),
      users: new FormControl([])
    });
  
    // Appellez la méthode submit
    component.submit();
  
    // Vérifiez que la fonction espion a été appelée avec l'objet session
    expect(createSpy).toHaveBeenCalledWith(session);
    expect(snackBarSpy).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });
});