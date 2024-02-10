import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { User } from '../../interfaces/user.interface';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  //Avant chaque test, on crée un nouveau module de test contenant le composant à tester et les providers nécessaires
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      //Spécifier le composant à tester
      declarations: [ MeComponent ],
      //Fournir les mocks aux fournisseurs de services nécessaires
      providers: [
        { provide: UserService, useValue: { getById: jest.fn(), delete: jest.fn() } }, //User
        { provide: SessionService, useValue: { sessionInformation: { id: '1' }, logOut: jest.fn() } }, //Session
        { provide: MatSnackBar, useValue: { open: jest.fn() } }, //Message pop-up
        { provide: Router, useValue: { navigate: jest.fn() } }, //Navigation
      ]
    })
    .compileComponents(); //Compiler le composant prend du temps, on utilise donc la méthode async
  });

  //Injecter les services nécessaires et récupérer le composant à tester
  beforeEach(() => {
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
  });

  //Vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //Vérifier que le composant récupère l'utilisateur au démarrage
  it('should get user on init', () => {
    const user: User = { 
      id: 1, 
      email: 'arthur@levesque.com', 
      lastName: 'levesque', 
      firstName: 'Arthur', 
      admin: false, 
      password: 'test!1234', 
      createdAt: new Date() 
    };
    jest.spyOn(userService, 'getById').mockReturnValue(of(user)); 
    component.ngOnInit();
    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(user);
  });

  //Vérifier que le composant supprime l'utilisateur
  it('should delete user', () => {
    jest.spyOn(userService, 'delete').mockReturnValue(of(null));
    component.delete();
    expect(userService.delete).toHaveBeenCalledWith('1'); 
    expect(matSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });

  //Vérifier que le composant navigue en arrière
  it('should navigate back', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });
});