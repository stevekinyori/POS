/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BrandsDetailComponent } from '../../../../../../main/webapp/app/entities/brands/brands-detail.component';
import { BrandsService } from '../../../../../../main/webapp/app/entities/brands/brands.service';
import { Brands } from '../../../../../../main/webapp/app/entities/brands/brands.model';

describe('Component Tests', () => {

    describe('Brands Management Detail Component', () => {
        let comp: BrandsDetailComponent;
        let fixture: ComponentFixture<BrandsDetailComponent>;
        let service: BrandsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [BrandsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BrandsService,
                    JhiEventManager
                ]
            }).overrideTemplate(BrandsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BrandsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BrandsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Brands(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.brands).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
