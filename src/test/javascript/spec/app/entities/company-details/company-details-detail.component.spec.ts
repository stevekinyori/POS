/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { COMPANY_DETAILSDetailComponent } from '../../../../../../main/webapp/app/entities/company-details/company-details-detail.component';
import { COMPANY_DETAILSService } from '../../../../../../main/webapp/app/entities/company-details/company-details.service';
import { COMPANY_DETAILS } from '../../../../../../main/webapp/app/entities/company-details/company-details.model';

describe('Component Tests', () => {

    describe('COMPANY_DETAILS Management Detail Component', () => {
        let comp: COMPANY_DETAILSDetailComponent;
        let fixture: ComponentFixture<COMPANY_DETAILSDetailComponent>;
        let service: COMPANY_DETAILSService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [COMPANY_DETAILSDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    COMPANY_DETAILSService,
                    JhiEventManager
                ]
            }).overrideTemplate(COMPANY_DETAILSDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(COMPANY_DETAILSDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(COMPANY_DETAILSService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new COMPANY_DETAILS(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cOMPANY_DETAILS).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
