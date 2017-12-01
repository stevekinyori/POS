import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Sales } from './sales.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SalesService {

    private resourceUrl = SERVER_API_URL + 'api/sales';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sales';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(sales: Sales): Observable<Sales> {
        const copy = this.convert(sales);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(sales: Sales): Observable<Sales> {
        const copy = this.convert(sales);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Sales> {
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
     * Convert a returned JSON object to Sales.
     */
    private convertItemFromServer(json: any): Sales {
        const entity: Sales = Object.assign(new Sales(), json);
        entity.dateInitiated = this.dateUtils
            .convertDateTimeFromServer(json.dateInitiated);
        entity.dateClosed = this.dateUtils
            .convertDateTimeFromServer(json.dateClosed);
        return entity;
    }

    /**
     * Convert a Sales to a JSON which can be sent to the server.
     */
    private convert(sales: Sales): Sales {
        const copy: Sales = Object.assign({}, sales);

        copy.dateInitiated = this.dateUtils.toDate(sales.dateInitiated);

        copy.dateClosed = this.dateUtils.toDate(sales.dateClosed);
        return copy;
    }
}
