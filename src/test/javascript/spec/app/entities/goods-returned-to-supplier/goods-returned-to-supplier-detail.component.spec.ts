/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GoodsReturnedToSupplierDetailComponent } from '../../../../../../main/webapp/app/entities/goods-returned-to-supplier/goods-returned-to-supplier-detail.component';
import { GoodsReturnedToSupplierService } from '../../../../../../main/webapp/app/entities/goods-returned-to-supplier/goods-returned-to-supplier.service';
import { GoodsReturnedToSupplier } from '../../../../../../main/webapp/app/entities/goods-returned-to-supplier/goods-returned-to-supplier.model';

describe('Component Tests', () => {

    describe('GoodsReturnedToSupplier Management Detail Component', () => {
        let comp: GoodsReturnedToSupplierDetailComponent;
        let fixture: ComponentFixture<GoodsReturnedToSupplierDetailComponent>;
        let service: GoodsReturnedToSupplierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [GoodsReturnedToSupplierDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GoodsReturnedToSupplierService,
                    JhiEventManager
                ]
            }).overrideTemplate(GoodsReturnedToSupplierDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GoodsReturnedToSupplierDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GoodsReturnedToSupplierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new GoodsReturnedToSupplier(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.goodsReturnedToSupplier).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
