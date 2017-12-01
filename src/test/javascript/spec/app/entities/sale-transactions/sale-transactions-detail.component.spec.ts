/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SaleTransactionsDetailComponent } from '../../../../../../main/webapp/app/entities/sale-transactions/sale-transactions-detail.component';
import { SaleTransactionsService } from '../../../../../../main/webapp/app/entities/sale-transactions/sale-transactions.service';
import { SaleTransactions } from '../../../../../../main/webapp/app/entities/sale-transactions/sale-transactions.model';

describe('Component Tests', () => {

    describe('SaleTransactions Management Detail Component', () => {
        let comp: SaleTransactionsDetailComponent;
        let fixture: ComponentFixture<SaleTransactionsDetailComponent>;
        let service: SaleTransactionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [SaleTransactionsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SaleTransactionsService,
                    JhiEventManager
                ]
            }).overrideTemplate(SaleTransactionsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleTransactionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleTransactionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SaleTransactions(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.saleTransactions).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
