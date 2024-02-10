import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockActivatedRoute: any;
  let mockSessionService: any;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockMatSnackBar: any;
  let mockRouter: any;

  // Avant chaque test, on configure le module de test
  beforeEach(async () => {
    // Créer des mocks pour les services et les classes utilisées dans le composant
    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn(() => '1') 
        }
      }
    };

    
    mockSessionService = {
      sessionInformation: { 
        token: 'token',
        type: 'type',
        id: 123,
        username: 'username',
        firstName: 'Arthur',
        lastName: 'levsque',
        admin: true
      } as SessionInformation
    };

    // Créer des mocks pour les méthodes de SessionApiService
    mockSessionApiService = {
      detail: jest.fn(() => of({ id: 1, users: [123] })),
      delete: jest.fn(() => of(null)),
      participate: jest.fn(() => of(null)),
      unParticipate: jest.fn(() => of(null))
    };

    mockTeacherService = {
      detail: jest.fn(() => of({ id: 1, firstName: 'Arthur', lastName: 'levesque' }))
    };

    mockMatSnackBar = {
      open: jest.fn()
    };

    mockRouter = {
      navigate: jest.fn()
    };

    // Configurer le module de test
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [HttpClientTestingModule],
      // Fournir les mocks aux fournisseurs de services nécessaires
      providers: [
        FormBuilder,
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();
  });

  // Injecter les services nécessaires et récupérer le composant à tester
  beforeEach(() => {
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance; 
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Vérifier que le composant récupère la session au démarrage
  it('should fetch session on ngOnInit', () => {
    component.ngOnInit();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  // Vérifier que le composant supprime la session
  it('should delete session', () => {
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  // Vérifier que le composant participe à la session
  it('should participate in session', () => {
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '123');
  });

  // Vérifier que le composant se désinscrit de la session
  it('should unparticipate in session', () => {
    component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '123');
  });

});
