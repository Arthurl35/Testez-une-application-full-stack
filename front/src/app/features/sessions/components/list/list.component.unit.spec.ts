import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockSessionApiService: any;
  let mockSessionService: any;

  beforeEach(async () => {
    mockSessionApiService = {
      all: jest.fn(() => of([])) 
    };

    mockSessionService = {
      sessionInformation: { admin: true, id: 123 } as SessionInformation 
    };

    await TestBed.configureTestingModule({
      declarations: [ ListComponent ],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Vérifier que la méthode all de SessionApiService est bien appelée lors de l'initialisation du composant
  it('should call sessionApiService.all() on initialization', () => {
    expect(mockSessionApiService.all).toHaveBeenCalled();
  });

  //Verifier que la propriété user du composant contient bien les données retournées par SessionService
  it('should return session information', () => {
    // Attendre que le composant ait été initialisé et que la méthode all de SessionApiService soit appelée
    fixture.whenStable().then(() => {
      // Vérifier que la méthode all de SessionApiService a bien été appelée
      expect(mockSessionApiService.all).toHaveBeenCalled();
      // Attendre que le composant mette à jour la propriété sessions$
      fixture.detectChanges();
      // Vérifier que la propriété sessions$ du composant contient bien les données retournées par SessionApiService
      expect(component.sessions$).toEqual(of([]));
    });
  });

  //Verifier que la propriété user du composant contient bien les données retournées par SessionService
  it('should return session information', () => {
    // Attendre que le composant ait été initialisé
    fixture.whenStable().then(() => {
      // Vérifier que la méthode all de SessionApiService a bien été appelée
      expect(mockSessionApiService.all).toHaveBeenCalled();
      // Attendre que le composant mette à jour la propriété sessions$
      fixture.detectChanges();
      // Vérifier que la propriété sessions$ du composant contient bien les données retournées par SessionApiService
      expect(component.sessions$).toEqual(of([]));
    });
  });

});

