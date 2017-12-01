/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SupplierProductsDetailComponent } from '../../../../../../main/webapp/app/entities/supplier-products/supplier-products-detail.component';
import { SupplierProductsService } from '../../../../../../main/webapp/app/entities/supplier-products/supplier-products.service';
import { SupplierProducts } from '../../../../../../main/webapp/app/entities/supplier-products/supplier-products.model';

describe('Component Tests', () => {

    describe('SupplierProducts Management Detail Component', () => {
        let comp: SupplierProductsDetailComponent;
        let fixture: ComponentFixture<SupplierProductsDetailComponent>;
        let service: SupplierProductsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [SupplierProductsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SupplierProductsService,
                    JhiEventManager
                ]
            }).overrideTemplate(SupplierProductsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SupplierProductsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SupplierProductsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SupplierProducts(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.supplierProducts).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
