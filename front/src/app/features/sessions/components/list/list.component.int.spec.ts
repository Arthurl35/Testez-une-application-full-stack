import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';
import { SessionService } from '../../../../services/session.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { ListComponent } from './list.component';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const sessionServiceStub = {
    get sessionInformation() {
      return {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      } as SessionInformation;
    }
  };

  // Création d'un faux service SessionApiService pour simuler ses comportements
  const sessionApiServiceStub = {
    all: () => of([{ id: 1, name: 'Session 1' }] as Session[])
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListComponent ],
      providers: [
        { provide: SessionService, useValue: sessionServiceStub },
        { provide: SessionApiService, useValue: sessionApiServiceStub }
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

  // Test pour vérifier que les informations de l'utilisateur sont correctement récupérées
  it('should get user information', () => {
    expect(component.user).toEqual(sessionServiceStub.sessionInformation);
  });

  // Test pour vérifier que les sessions sont correctement récupérées
  it('should get sessions', (done) => {
    component.sessions$.subscribe(sessions => {
      expect(sessions).toEqual([{ id: 1, name: 'Session 1' }]);
      done();
    });
  });
});