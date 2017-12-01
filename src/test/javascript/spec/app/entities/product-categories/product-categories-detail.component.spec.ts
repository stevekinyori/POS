/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProductCategoriesDetailComponent } from '../../../../../../main/webapp/app/entities/product-categories/product-categories-detail.component';
import { ProductCategoriesService } from '../../../../../../main/webapp/app/entities/product-categories/product-categories.service';
import { ProductCategories } from '../../../../../../main/webapp/app/entities/product-categories/product-categories.model';

describe('Component Tests', () => {

    describe('ProductCategories Management Detail Component', () => {
        let comp: ProductCategoriesDetailComponent;
        let fixture: ComponentFixture<ProductCategoriesDetailComponent>;
        let service: ProductCategoriesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [ProductCategoriesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProductCategoriesService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProductCategoriesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProductCategoriesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductCategoriesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProductCategories(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.productCategories).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
