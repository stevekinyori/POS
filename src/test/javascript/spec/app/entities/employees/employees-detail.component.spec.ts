/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmployeesDetailComponent } from '../../../../../../main/webapp/app/entities/employees/employees-detail.component';
import { EmployeesService } from '../../../../../../main/webapp/app/entities/employees/employees.service';
import { Employees } from '../../../../../../main/webapp/app/entities/employees/employees.model';

describe('Component Tests', () => {

    describe('Employees Management Detail Component', () => {
        let comp: EmployeesDetailComponent;
        let fixture: ComponentFixture<EmployeesDetailComponent>;
        let service: EmployeesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [EmployeesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmployeesService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmployeesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployeesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Employees(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.employees).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
