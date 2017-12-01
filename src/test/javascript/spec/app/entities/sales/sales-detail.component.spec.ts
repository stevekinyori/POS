/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SalesDetailComponent } from '../../../../../../main/webapp/app/entities/sales/sales-detail.component';
import { SalesService } from '../../../../../../main/webapp/app/entities/sales/sales.service';
import { Sales } from '../../../../../../main/webapp/app/entities/sales/sales.model';

describe('Component Tests', () => {

    describe('Sales Management Detail Component', () => {
        let comp: SalesDetailComponent;
        let fixture: ComponentFixture<SalesDetailComponent>;
        let service: SalesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [SalesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SalesService,
                    JhiEventManager
                ]
            }).overrideTemplate(SalesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Sales(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.sales).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
