import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ProductCategories } from './product-categories.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProductCategoriesService {

    private resourceUrl = SERVER_API_URL + 'api/product-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/product-categories';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(productCategories: ProductCategories): Observable<ProductCategories> {
        const copy = this.convert(productCategories);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(productCategories: ProductCategories): Observable<ProductCategories> {
        const copy = this.convert(productCategories);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ProductCategories> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to ProductCategories.
     */
    private convertItemFromServer(json: any): ProductCategories {
        const entity: ProductCategories = Object.assign(new ProductCategories(), json);
        entity.dateCreated = this.dateUtils
            .convertDateTimeFromServer(json.dateCreated);
        return entity;
    }

    /**
     * Convert a ProductCategories to a JSON which can be sent to the server.
     */
    private convert(productCategories: ProductCategories): ProductCategories {
        const copy: ProductCategories = Object.assign({}, productCategories);

        copy.dateCreated = this.dateUtils.toDate(productCategories.dateCreated);
        return copy;
    }
}
