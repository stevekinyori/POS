/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SuppliersDetailComponent } from '../../../../../../main/webapp/app/entities/suppliers/suppliers-detail.component';
import { SuppliersService } from '../../../../../../main/webapp/app/entities/suppliers/suppliers.service';
import { Suppliers } from '../../../../../../main/webapp/app/entities/suppliers/suppliers.model';

describe('Component Tests', () => {

    describe('Suppliers Management Detail Component', () => {
        let comp: SuppliersDetailComponent;
        let fixture: ComponentFixture<SuppliersDetailComponent>;
        let service: SuppliersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [SuppliersDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SuppliersService,
                    JhiEventManager
                ]
            }).overrideTemplate(SuppliersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SuppliersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SuppliersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Suppliers(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.suppliers).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
