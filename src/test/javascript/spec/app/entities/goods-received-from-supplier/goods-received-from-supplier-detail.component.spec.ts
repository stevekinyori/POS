/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GoodsReceivedFromSupplierDetailComponent } from '../../../../../../main/webapp/app/entities/goods-received-from-supplier/goods-received-from-supplier-detail.component';
import { GoodsReceivedFromSupplierService } from '../../../../../../main/webapp/app/entities/goods-received-from-supplier/goods-received-from-supplier.service';
import { GoodsReceivedFromSupplier } from '../../../../../../main/webapp/app/entities/goods-received-from-supplier/goods-received-from-supplier.model';

describe('Component Tests', () => {

    describe('GoodsReceivedFromSupplier Management Detail Component', () => {
        let comp: GoodsReceivedFromSupplierDetailComponent;
        let fixture: ComponentFixture<GoodsReceivedFromSupplierDetailComponent>;
        let service: GoodsReceivedFromSupplierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [GoodsReceivedFromSupplierDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GoodsReceivedFromSupplierService,
                    JhiEventManager
                ]
            }).overrideTemplate(GoodsReceivedFromSupplierDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GoodsReceivedFromSupplierDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GoodsReceivedFromSupplierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new GoodsReceivedFromSupplier(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.goodsReceivedFromSupplier).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
