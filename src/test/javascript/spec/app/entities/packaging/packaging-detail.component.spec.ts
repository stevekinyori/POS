/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PackagingDetailComponent } from '../../../../../../main/webapp/app/entities/packaging/packaging-detail.component';
import { PackagingService } from '../../../../../../main/webapp/app/entities/packaging/packaging.service';
import { Packaging } from '../../../../../../main/webapp/app/entities/packaging/packaging.model';

describe('Component Tests', () => {

    describe('Packaging Management Detail Component', () => {
        let comp: PackagingDetailComponent;
        let fixture: ComponentFixture<PackagingDetailComponent>;
        let service: PackagingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PosTestModule],
                declarations: [PackagingDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PackagingService,
                    JhiEventManager
                ]
            }).overrideTemplate(PackagingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PackagingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PackagingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Packaging(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.packaging).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
